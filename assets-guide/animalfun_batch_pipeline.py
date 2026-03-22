#!/usr/bin/env python3
"""
AnimalFun image pipeline using the direct OpenAI Image API.

Features
- Parse the markdown prompt file as the source of truth
- Keep a locked global style prefix for consistency
- Save all PNG files directly under the outputs folder
- Generate one image request per animal with /v1/images/generations
- Save one standalone PNG per animal
- Resize to 512x512
- Run simple QA checks

Usage examples
--------------
1) Prepare local manifest/files only:
   python animalfun_batch_pipeline.py prepare \
       --markdown /mnt/data/image-prompts.md

2) Generate all missing images:
   python animalfun_batch_pipeline.py generate \
       --markdown /mnt/data/image-prompts.md

3) Generate a single animal only:
   python animalfun_batch_pipeline.py generate \
       --markdown /mnt/data/image-prompts.md \
       --only cow

4) Run QA on generated images:
   python animalfun_batch_pipeline.py qa

5) End-to-end run:
   python animalfun_batch_pipeline.py run-all \
       --markdown /mnt/data/image-prompts.md
"""

from __future__ import annotations

import argparse
import base64
import io
import json
import os
import re
import sys
import time
from dataclasses import dataclass
from pathlib import Path
from typing import Dict, List, Optional, Tuple

from dotenv import load_dotenv
from openai import OpenAI
from PIL import Image

SCRIPT_DIR = Path(__file__).resolve().parent
load_dotenv(SCRIPT_DIR / ".env")
load_dotenv()

LOCKED_STYLE_PREFIX = (
    "Create a single image: "
    "cute cartoon animal, flat vector illustration, bright cheerful colors, "
    "pure white background, centered full body, friendly child-safe expression, "
    "simple bold shapes, minimal detail, clean high-contrast composition, "
    "consistent educational app asset style for ages 3-8, "
    "no text, no logos, no watermark, no scenery, no props, no extra animals, "
    "square composition. "
)

LOCKED_NEGATIVE_GUIDANCE = (
    "Avoid realism, gradients, painterly rendering, dramatic lighting, "
    "backgrounds, scenery, props, clutter, shadows, text, logos, watermarks, "
    "multiple animals, and scary expressions."
)

API_IMAGE_SIZE = "1024x1024"
FINAL_IMAGE_SIZE = (512, 512)
IMAGE_MODEL = os.getenv("OPENAI_IMAGE_MODEL", "gpt-image-1-mini")
IMAGE_QUALITY = os.getenv("OPENAI_IMAGE_QUALITY", "medium")
DEFAULT_BATCH_SIZE = 5
REQUEST_DELAY_SECONDS = float(os.getenv("OPENAI_IMAGE_REQUEST_DELAY", "0"))

WORK_DIR = SCRIPT_DIR / "animalfun_work"
STATE_DIR = WORK_DIR / "state"
OUTPUTS_DIR = WORK_DIR / "outputs"
QA_DIR = WORK_DIR / "qa"
MANIFEST_PATH = STATE_DIR / "manifest.json"
GENERATION_STATE_PATH = STATE_DIR / "generation_state.json"


@dataclass
class AnimalPrompt:
    category: str
    animal_name: str
    raw_prompt: str

    @property
    def file_stem(self) -> str:
        return slugify(self.animal_name)

    @property
    def final_filename(self) -> str:
        return f"{self.file_stem}.png"

    def prompt_without_prefix(self) -> str:
        text = self.raw_prompt.strip()
        prefix = "Create a single image:"
        if text.lower().startswith(prefix.lower()):
            return text[len(prefix):].strip()
        return text

    def final_prompt(self) -> str:
        return (
            LOCKED_STYLE_PREFIX
            + self.prompt_without_prefix().rstrip(". ")
            + ". "
            + LOCKED_NEGATIVE_GUIDANCE
        )




def ensure_dirs() -> None:
    for d in [WORK_DIR, STATE_DIR, OUTPUTS_DIR, QA_DIR]:
        d.mkdir(parents=True, exist_ok=True)


def slugify(name: str) -> str:
    s = name.strip().lower()
    s = s.replace("&", "and")
    s = re.sub(r"[^a-z0-9]+", "_", s)
    s = re.sub(r"_+", "_", s).strip("_")
    return s


def load_client() -> OpenAI:
    api_key = os.getenv("OPENAI_API_KEY")
    if not api_key:
        raise RuntimeError("OPENAI_API_KEY not found. Put it in your .env file.")
    return OpenAI(api_key=api_key)


def load_json(path: Path, default):
    if path.exists():
        with open(path, "r", encoding="utf-8") as f:
            return json.load(f)
    return default


def save_json(path: Path, data) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=2, ensure_ascii=False)


def parse_markdown_prompts(markdown_path: Path) -> List[AnimalPrompt]:
    text = markdown_path.read_text(encoding="utf-8")
    lines = text.splitlines()
    prompts: List[AnimalPrompt] = []
    current_category: Optional[str] = None
    current_animal: Optional[str] = None

    i = 0
    while i < len(lines):
        line = lines[i].strip()

        if line.startswith("## "):
            current_category = line[3:].strip()
            i += 1
            continue

        if line.startswith("### "):
            current_animal = line[4:].strip()
            j = i + 1
            prompt_lines = []

            while j < len(lines):
                next_line = lines[j].strip()
                if next_line.startswith("### ") or next_line.startswith("## "):
                    break
                if next_line.startswith("Create a single image:"):
                    prompt_lines.append(next_line)
                    k = j + 1
                    while k < len(lines):
                        cont = lines[k].strip()
                        if not cont:
                            break
                        if cont.startswith("### ") or cont.startswith("## "):
                            break
                        if cont.startswith("If ChatGPT adds backgrounds") or cont.startswith("Avoid realism"):
                            break
                        if cont.startswith("Create a single image:") and k != j:
                            break
                        prompt_lines.append(cont)
                        k += 1
                    j = k
                    break
                j += 1

            raw_prompt = " ".join(prompt_lines).strip()
            if current_category and current_animal and raw_prompt:
                prompts.append(AnimalPrompt(current_category, current_animal, raw_prompt))
            i = j
            continue

        i += 1

    if not prompts:
        raise ValueError(f"No prompts found in markdown file: {markdown_path}")

    return prompts




def prepare_batches(markdown_path: Path) -> None:
    ensure_dirs()
    prompts = parse_markdown_prompts(markdown_path)
    manifest = [
        {
            "animal_name": p.animal_name,
            "file_stem": p.file_stem,
            "category": p.category,
            "filename": p.final_filename,
        }
        for p in prompts
    ]
    save_json(MANIFEST_PATH, manifest)
    if not GENERATION_STATE_PATH.exists():
        save_json(GENERATION_STATE_PATH, {})
    print(f"[OK] Prepared manifest for {len(prompts)} animals")
    print(f"[OK] Manifest saved to: {MANIFEST_PATH}")


def manifest_lookup() -> Dict[str, str]:
    manifest = load_json(MANIFEST_PATH, [])
    if not manifest:
        raise RuntimeError("Manifest not found. Run `prepare` first.")

    return {entry["file_stem"]: str(OUTPUTS_DIR) for entry in manifest}


def decode_and_save_png(image_b64: str, output_path: Path) -> None:
    image_bytes = base64.b64decode(image_b64)
    img = Image.open(io.BytesIO(image_bytes)).convert("RGBA")
    final_img = Image.new("RGBA", img.size, (255, 255, 255, 255))
    final_img.alpha_composite(img)
    final_img = final_img.convert("RGB")
    final_img = final_img.resize(FINAL_IMAGE_SIZE, Image.LANCZOS)
    output_path.parent.mkdir(parents=True, exist_ok=True)
    final_img.save(output_path, format="PNG", optimize=True)


def generate_one(client: OpenAI, prompt: AnimalPrompt, output_folder: Path) -> Path:
    result = client.images.generate(
        model=IMAGE_MODEL,
        prompt=prompt.final_prompt(),
        size=API_IMAGE_SIZE,
        quality=IMAGE_QUALITY,
        background="opaque",
    )

    if not getattr(result, "data", None) or not getattr(result.data[0], "b64_json", None):
        raise RuntimeError(f"No image data returned for {prompt.animal_name}")

    output_path = output_folder / prompt.final_filename
    decode_and_save_png(result.data[0].b64_json, output_path)
    return output_path


def generate_images(markdown_path: Path, only: Optional[str], force: bool) -> None:
    ensure_dirs()
    prepare_batches(markdown_path)
    prompts = parse_markdown_prompts(markdown_path)
    output_lookup = manifest_lookup()
    state = load_json(GENERATION_STATE_PATH, {})
    client = load_client()

    target_slug = slugify(only) if only else None
    generated = 0
    skipped = 0

    for prompt in prompts:
        if target_slug and prompt.file_stem != target_slug:
            continue

        output_folder = Path(output_lookup[prompt.file_stem])
        output_path = output_folder / prompt.final_filename

        if output_path.exists() and not force:
            print(f"[SKIP] Exists: {output_path}")
            skipped += 1
            continue

        print(f"[INFO] Generating {prompt.animal_name} -> {output_path.name}")
        saved_path = generate_one(client, prompt, output_folder)
        state[prompt.file_stem] = {
            "animal_name": prompt.animal_name,
            "category": prompt.category,
            "output_path": str(saved_path),
            "generated_at": int(time.time()),
            "model": IMAGE_MODEL,
            "quality": IMAGE_QUALITY,
        }
        save_json(GENERATION_STATE_PATH, state)
        print(f"[OK] Saved {saved_path}")
        generated += 1

        if REQUEST_DELAY_SECONDS > 0:
            time.sleep(REQUEST_DELAY_SECONDS)

    if generated == 0 and skipped == 0 and target_slug:
        raise RuntimeError(f"Animal not found in markdown: {only}")

    print(f"[OK] Generated {generated} image(s), skipped {skipped}")


def sample_pixels_for_white_bg(img: Image.Image) -> Tuple[bool, Dict[str, Tuple[int, int, int]]]:
    rgb = img.convert("RGB")
    w, h = rgb.size
    points = {
        "top_left": (5, 5),
        "top_right": (w - 6, 5),
        "bottom_left": (5, h - 6),
        "bottom_right": (w - 6, h - 6),
        "mid_top": (w // 2, 5),
        "mid_bottom": (w // 2, h - 6),
        "mid_left": (5, h // 2),
        "mid_right": (w - 6, h // 2),
    }
    samples = {name: rgb.getpixel(pt) for name, pt in points.items()}
    ok = all(all(channel >= 245 for channel in px) for px in samples.values())
    return ok, samples


def run_qa() -> None:
    ensure_dirs()
    qa_report = {}
    for png_path in sorted(OUTPUTS_DIR.glob("*.png")):
        img = Image.open(png_path)
        size_ok = img.size == FINAL_IMAGE_SIZE
        white_ok, samples = sample_pixels_for_white_bg(img)
        qa_report[png_path.name] = {
            "size": img.size,
            "size_ok": size_ok,
            "white_background_heuristic_ok": white_ok,
            "sampled_pixels": samples,
        }
    qa_path = QA_DIR / "qa_report.json"
    save_json(qa_path, qa_report)
    print(f"[OK] QA report written to: {qa_path}")


def print_status() -> None:
    state = load_json(GENERATION_STATE_PATH, {})
    manifest = load_json(MANIFEST_PATH, [])
    expected = len(manifest) if manifest else 0
    print(f"Generated: {len(state)} / {expected}")
    for key, value in sorted(state.items()):
        print(f"- {key}: {value['output_path']}")


def cmd_prepare(args):
    prepare_batches(Path(args.markdown))


def cmd_generate(args):
    generate_images(Path(args.markdown), args.only, args.force)


def cmd_status(args):
    print_status()


def cmd_qa(args):
    run_qa()




def cmd_run_all(args):
    generate_images(Path(args.markdown), args.only, args.force)
    run_qa()



def build_parser():
    parser = argparse.ArgumentParser(description="AnimalFun direct Image API pipeline")
    sub = parser.add_subparsers(dest="command", required=True)

    p_prepare = sub.add_parser("prepare", help="Parse markdown and prepare local manifest")
    p_prepare.add_argument("--markdown", required=True, help="Path to AnimalFun markdown file")
    p_prepare.set_defaults(func=cmd_prepare)

    p_generate = sub.add_parser("generate", help="Generate images directly with the Image API")
    p_generate.add_argument("--markdown", required=True, help="Path to AnimalFun markdown file")
    p_generate.add_argument("--only", help="Generate only one animal, for example cow")
    p_generate.add_argument("--force", action="store_true", help="Regenerate even if the PNG already exists")
    p_generate.set_defaults(func=cmd_generate)

    p_status = sub.add_parser("status", help="Show local generation progress")
    p_status.set_defaults(func=cmd_status)

    p_qa = sub.add_parser("qa", help="Run QA on generated images")
    p_qa.set_defaults(func=cmd_qa)


    p_run = sub.add_parser("run-all", help="Prepare, generate, and run QA")
    p_run.add_argument("--markdown", required=True, help="Path to AnimalFun markdown file")
    p_run.add_argument("--only", help="Generate only one animal, for example cow")
    p_run.add_argument("--force", action="store_true", help="Regenerate even if the PNG already exists")
    p_run.set_defaults(func=cmd_run_all)

    return parser



def main():
    parser = build_parser()
    args = parser.parse_args()
    ensure_dirs()
    try:
        args.func(args)
    except KeyboardInterrupt:
        print("\n[INTERRUPTED]")
        sys.exit(130)
    except Exception as e:
        print(f"[ERROR] {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
