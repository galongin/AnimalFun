# AnimalFun

An interactive Android app for kids (ages 3-8) to learn about animals. Supports English and Hebrew with full RTL layout support. Works completely offline.

## Features

- **Explore** - Browse 50 animals across 6 categories (Farm, Jungle, Ocean, Birds, Pets, Insects)
- **Animal Details** - Large illustrations, fun facts, sounds, habitat/diet/size info
- **Quiz Mode** - 6 quiz types (Picture, Sound, Riddle, Diet, Habitat, Reverse) with 3 difficulty levels
- **Sounds Game** - Explore animal sounds and play "Match the Sound"
- **Memory Game** - Card matching with flip animations, timer, and adaptive grid sizes
- **Progress Tracking** - Star rewards, per-category progress, quiz accuracy stats
- **Settings** - Parental gate (math problem), language toggle (EN/HE), sound toggle, progress reset
- **Bilingual** - Full English and Hebrew support with automatic RTL layout switching
- **Responsive** - Adapts to phones and tablets (2/3/4 column grids, bottom bar vs side rail)

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **DI:** Koin
- **Database:** Room (pre-populated, offline)
- **Preferences:** DataStore
- **Responsive:** Material 3 Window Size Classes

## Building

```bash
# Debug build
./gradlew assembleDebug

# Run unit tests
./gradlew testDebugUnitTest

# Install on connected device/emulator
./gradlew installDebug
```

## Project Structure

```
app/src/main/java/com/animalfun/
├── data/
│   ├── local/          # Room DB, DAOs, TypeConverters, SeedData
│   ├── model/          # Animal, QuizQuestion, UserProgress entities
│   └── repository/     # AnimalRepository, ProgressRepository
├── di/                 # Koin dependency injection modules
├── ui/
│   ├── components/     # AnimalCard, QuizOptionButton, CelebrationOverlay, ResponsiveGrid
│   ├── navigation/     # NavGraph, Screen routes
│   ├── screens/        # Home, Explore, Detail, Quiz, Sounds, Memory, Settings
│   └── theme/          # Colors, Typography, Material 3 Theme
└── util/               # AudioPlayer, LocaleHelper
```

## Asset Replacement

Animal illustrations are currently 512x512 PNG placeholders in `drawable-xxhdpi/`. See `assets-guide/image-prompts.md` for AI image generation prompts covering all 50 animals.
