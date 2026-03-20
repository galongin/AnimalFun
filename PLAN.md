# AnimalFun — Android App Implementation Plan

## Context
Build an interactive Android app for kids (ages 3–8) to learn about animals. The app supports English and Hebrew (one at a time, toggled in settings with RTL direction change). It must work fully offline with no backend — all data, images, and sounds bundled in the APK. It must support different screen sizes (phones and tablets).

---

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **DI:** Koin (lightweight, simpler than Hilt for this project size)
- **Local DB:** Room (animal data, quiz content, progress tracking)
- **Preferences:** DataStore (language setting, sound toggle)
- **Animations:** Lottie Compose (celebration effects)
- **Responsive:** Material 3 Window Size Classes (Compact/Medium/Expanded)
- **No network libraries** — fully offline

---

## Phase 1 — Project Scaffold & Core Screens

### Step 1: Initialize Android Project
- Create standard Android project structure with Kotlin + Compose
- **Files to create:**
  - `build.gradle.kts` (project-level)
  - `app/build.gradle.kts` (app-level with all dependencies)
  - `settings.gradle.kts`
  - `gradle.properties`
  - `app/src/main/AndroidManifest.xml`
  - `app/src/main/java/com/animalfun/AnimalFunApp.kt` (Application class)
  - `app/src/main/java/com/animalfun/MainActivity.kt`

### Step 2: Theme & Design System
- Kid-friendly color palette (bright, high contrast)
- Large touch targets (56dp minimum for kids)
- Font sizes: 18sp body minimum, 24sp+ for animal names
- Rounded shapes, playful feel
- **Files:**
  - `ui/theme/Color.kt`
  - `ui/theme/Type.kt`
  - `ui/theme/Theme.kt`

### Step 3: Navigation
- Bottom navigation bar (Compact/Medium), side rail (Expanded)
- Routes: Home, Explore, Quiz, Sounds, Memory, Settings, AnimalDetail
- **Files:**
  - `ui/navigation/NavGraph.kt`
  - `ui/navigation/Screen.kt`

### Step 4: Bilingual Support (English ↔ Hebrew)
- `values/strings.xml` (English) and `values-iw/strings.xml` (Hebrew)
- In-app language toggle using `AppCompatDelegate.setApplicationLocales()`
- Layout direction flips (LTR ↔ RTL) via `CompositionLocalProvider(LocalLayoutDirection)`
- Language preference persisted in DataStore
- **Files:**
  - `res/values/strings.xml`
  - `res/values-iw/strings.xml`
  - `util/LocaleHelper.kt`

### Step 5: Data Model & Local Database
- Room DB with pre-populated data (shipped via `createFromAsset`)
- Entities: Animal, QuizQuestion, UserProgress
- **Files:**
  - `data/model/Animal.kt`
  - `data/model/QuizQuestion.kt`
  - `data/model/UserProgress.kt`
  - `data/local/AnimalDatabase.kt`
  - `data/local/AnimalDao.kt`
  - `data/repository/AnimalRepository.kt`

### Step 6: Koin DI Setup
- Modules for database, repository, viewmodels
- **Files:**
  - `di/AppModule.kt`

### Step 7: Home Screen
- Colorful grid of activity cards (Explore, Quiz, Sounds, Memory Game)
- Language flag toggle in top bar
- Progress stars showing overall completion
- Responsive: 2 columns on phone, 3+ on tablet
- **Files:**
  - `ui/screens/home/HomeScreen.kt`
  - `ui/screens/home/HomeViewModel.kt`

### Step 8: Explore Screen
- Categories: Farm, Jungle, Ocean, Birds, Pets, Insects
- Scrollable grid of animal cards (2 cols phone / 3 tablet / 4 large tablet)
- Category filter chips at top
- **Files:**
  - `ui/screens/explore/ExploreScreen.kt`
  - `ui/screens/explore/ExploreViewModel.kt`

### Step 9: Animal Detail Screen
- Large illustration, name in current language
- "Tap to hear" button — plays animal sound + name pronunciation
- Fun facts as swipeable cards
- Habitat, diet, size info with icons
- **Files:**
  - `ui/screens/detail/AnimalDetailScreen.kt`
  - `ui/screens/detail/AnimalDetailViewModel.kt`
  - `util/AudioPlayer.kt`

### Step 10: Initial Animal Content (10 animals)
- 2 per category to validate the full flow
- Placeholder vector drawable images (colored silhouettes/simple shapes)
- Placeholder sound files (short beep tones as stand-ins)
- English + Hebrew strings for names, facts, riddles
- **Files:**
  - `assets/animals.db` (pre-populated Room DB)
  - `res/drawable/` (placeholder animal vectors)
  - `res/raw/` (placeholder sounds)

### Asset Replacement Guide (for later)
- **AI image prompts:** A file with 50 ready-to-use prompts for AI image generators (Nano Banana, Midjourney, DALL-E). Each prompt follows a consistent style: *"Cute cartoon [animal], flat design, bright colors, white background, children's educational app, vector illustration style"*
- **Sound sources:** Freesound.org (CC-licensed), BBC Sound Effects (free for personal use)
- **File:** `assets-guide/image-prompts.md` — batch prompts for all 50 animals
- Images should be exported as PNG 512x512 and placed in `drawable-xxhdpi/`

---

## Phase 2 — Games & Quizzes

### Step 11: Quiz Mode
Six quiz types, all multiple choice (4 options):
1. **Picture Quiz** — show image → pick correct name
2. **Sound Quiz** — play sound → pick correct animal
3. **Riddle Quiz** — "I have stripes and live in Africa" → pick animal
4. **Diet Quiz** — "What does a rabbit eat?" → pick food
5. **Habitat Quiz** — "Where does a dolphin live?" → pick place
6. **Reverse Quiz** — "Who lives in the ocean?" → pick animal image

- Difficulty: Easy (2 choices), Medium (3), Hard (4)
- Animated celebrations on correct answer (Lottie confetti)
- **Files:**
  - `ui/screens/quiz/QuizScreen.kt`
  - `ui/screens/quiz/QuizViewModel.kt`
  - `ui/screens/quiz/QuizType.kt`
  - `data/model/QuizQuestion.kt`

### Step 12: Animal Sounds Game
- Grid of animals — tap to hear sound
- "Match the sound" mode: hear sound → tap correct animal
- Streak counter with animations
- **Files:**
  - `ui/screens/sounds/SoundsScreen.kt`
  - `ui/screens/sounds/SoundsViewModel.kt`

### Step 13: Memory Card Game
- Match pairs: image ↔ image or image ↔ name (in current language)
- Grid sizes adapt to screen: 2x3 → 3x4 → 4x5
- Timer + move counter
- **Files:**
  - `ui/screens/memory/MemoryScreen.kt`
  - `ui/screens/memory/MemoryViewModel.kt`
  - `ui/screens/memory/MemoryGameState.kt`

### Step 14: Reusable UI Components
- `AnimalCard` composable (used in Explore, Sounds, Quiz)
- `QuizOptionButton` composable (correct/incorrect animation states)
- `CelebrationOverlay` composable (Lottie confetti/stars)
- `ResponsiveGrid` composable (adapts columns to window size class)
- **Files:**
  - `ui/components/AnimalCard.kt`
  - `ui/components/QuizOptionButton.kt`
  - `ui/components/CelebrationOverlay.kt`
  - `ui/components/ResponsiveGrid.kt`

---

## Phase 3 — Progress, Settings & Full Content

### Step 15: Progress Tracking
- Room DB stores quiz results, explored animals, best times
- Star reward system: earn stars for correct answers, exploring new animals
- Display on Home screen
- **Files:**
  - `data/local/ProgressDao.kt`
  - `data/repository/ProgressRepository.kt`

### Step 16: Settings Screen
- Language toggle (English ↔ Hebrew) with immediate UI/RTL switch
- Sound effects on/off
- Parental gate (simple math problem like "12+7=?") to enter settings
- Reset progress option
- **Files:**
  - `ui/screens/settings/SettingsScreen.kt`
  - `ui/screens/settings/SettingsViewModel.kt`

### Step 17: Full Animal Content (50 animals)
- Farm (8), Jungle (10), Ocean (8), Birds (8), Pets (8), Insects (8)
- Each animal: image, sound, 3–5 facts (EN+HE), riddle, diet, habitat
- All quiz questions for all 50 animals

---

## Phase 4 — Testing & Verification

### Unit Tests
- **ViewModel tests:** quiz logic, score calculation, progress tracking
- **Repository tests:** data retrieval, filtering by category
- **Locale tests:** verify string switching between EN/HE
- **Files:**
  - `app/src/test/.../quiz/QuizViewModelTest.kt`
  - `app/src/test/.../memory/MemoryViewModelTest.kt`
  - `app/src/test/.../repository/AnimalRepositoryTest.kt`
- **Run:** `./gradlew test`

### Instrumented / UI Tests
- **Navigation test:** verify all screen transitions
- **Language toggle test:** switch to Hebrew → verify RTL + Hebrew strings → switch back
- **Quiz flow test:** complete a quiz, verify score updates
- **Screen size tests:** run on Compact/Medium/Expanded emulator configs
- **Files:**
  - `app/src/androidTest/.../NavigationTest.kt`
  - `app/src/androidTest/.../LanguageToggleTest.kt`
  - `app/src/androidTest/.../QuizFlowTest.kt`
- **Run:** `./gradlew connectedAndroidTest`

### Manual Testing Checklist
- [ ] Build debug APK: `./gradlew assembleDebug`
- [ ] Install on phone via USB / Android Studio "Run"
- [ ] Test on emulators: Pixel 5 (compact), Pixel Tablet (expanded), Nexus 7 (medium)
- [ ] Verify all 6 quiz types work correctly
- [ ] Toggle language EN→HE: all text switches, RTL layout applies
- [ ] Toggle language HE→EN: all text switches back, LTR layout applies
- [ ] Play animal sounds on detail screen
- [ ] Complete memory game at each grid size
- [ ] Verify progress stars accumulate on home screen
- [ ] Test parental gate blocks access, correct answer grants entry
- [ ] Airplane mode: verify everything works offline
- [ ] Rotate device: verify layout adapts

### Building & Running
```bash
# Debug build (for testing)
./gradlew assembleDebug
# APK at: app/build/outputs/apk/debug/app-debug.apk

# Run all unit tests
./gradlew test

# Run on connected device/emulator
./gradlew installDebug

# Release build (for Play Store, requires signing config)
./gradlew assembleRelease
```

---

## Responsive Layout Summary

| Element | Compact (Phone) | Medium (Small Tablet) | Expanded (Large Tablet) |
|---|---|---|---|
| Animal grid | 2 columns | 3 columns | 4 columns |
| Quiz answers | 2x2 grid | Row of 4 | Row of 4, larger |
| Memory grid | 2x3 / 3x4 | 3x4 / 4x5 | 4x5 / 5x6 |
| Navigation | Bottom bar | Bottom bar | Side rail |
| Touch targets | 56dp | 56dp | 60dp |
| Body font | 18sp | 20sp | 22sp |

---

## Dependencies (final)
```kotlin
// Compose + Material 3
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material3:material3-window-size-class")
implementation("androidx.navigation:navigation-compose")
implementation("androidx.activity:activity-compose")

// Local data
implementation("androidx.room:room-runtime")
implementation("androidx.room:room-ktx")
ksp("androidx.room:room-compiler")
implementation("androidx.datastore:datastore-preferences")

// DI
implementation("io.insert-koin:koin-androidx-compose")

// Animations
implementation("com.airbnb.android:lottie-compose")

// Locale
implementation("androidx.appcompat:appcompat")

// Testing
testImplementation("junit:junit")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
testImplementation("io.mockk:mockk")
androidTestImplementation("androidx.compose.ui:ui-test-junit4")
androidTestImplementation("androidx.test.ext:junit")
```
