# Calculator for Android

A modern, full-featured Android Calculator application built with **Jetpack Compose**, **Material 3**, and clean reactive architecture (StateFlow & ViewModel), maintaining complete compatibility with standard Android Studio and Gradle toolchains.

---

## 📱 Features

- **Modern Jetpack Compose UI**: Tactile squircle buttons, soft shadows, and clean typography matching design specifications (`demo69.png`).
- **Edge-to-Edge Experience**: Fully transparent system status and navigation bars with insets handling.
- **Reactive Calculation Engine**: Live expression preview, operator precedence (`×` and `÷` before `+` and `-`), percentages, negative sign support, and formatted results.
- **Dual UI Implementations**:
  - `MainActivity.kt`: Primary Jetpack Compose + Material 3 implementation.
  - `MainActivityXml.java`: Classic Android Views / XML layout implementation (`res/layout/activity_main.xml`).
- **Comprehensive Unit Tests**: JUnit test coverage for mathematical evaluation, edge cases, and operator precedence in `CalculatorViewModelTest.kt`.

---

## 📁 Project Structure

```
calculator/
├── app/
│   ├── build.gradle.kts           # App module build configuration
│   ├── proguard-rules.pro         # Proguard rules
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml # App manifest
│       │   ├── java/com/mlab/calculator/
│       │   │   ├── MainActivity.kt        # Jetpack Compose Launcher Activity
│       │   │   ├── MainActivityXml.java   # Classic View Activity
│       │   │   └── compose/
│       │   │       ├── CalculatorComponents.kt  # Compose button, display & toolbar components
│       │   │       ├── CalculatorScreen.kt      # Main keypad & display layout
│       │   │       ├── CalculatorViewModel.kt   # Calculation state & evaluation logic
│       │   │       └── Theme.kt                 # Theme, palette & color definitions
│       │   └── res/
│       │       ├── drawable/       # Vector drawables for buttons & icons
│       │       ├── layout/         # activity_main.xml layout
│       │       └── values/         # strings.xml, colors.xml, styles.xml
│       ├── test/                   # Local Unit tests (JUnit 4)
│       └── androidTest/            # Instrumented UI tests
├── gradle/
│   ├── libs.versions.toml         # Version catalog (AGP, Kotlin, Compose, AndroidX)
│   └── wrapper/                   # Gradle wrapper configuration & JAR
├── build.gradle.kts               # Root build file
├── settings.gradle.kts            # Root settings file
├── gradle.properties              # Project JVM & AndroidX settings
├── gradlew / gradlew.bat          # Gradle wrapper executables
└── build.sh                       # Standalone Termux/AAPT2/D8 build script
```

---

## 🛠️ Building & Running

### Using Gradle (Android Studio / CLI)

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Install to connected device
./gradlew installDebug
```

### Using Standalone Build Script (Termux / Lightweight)

```bash
# Build APK using aapt2 + javac + d8
./build.sh

# Build, install, and launch via Shizuku/rish ADB
./build.sh --run
```

---

## 🧪 Technology Stack

- **Language**: Kotlin 2.0+ & Java 17
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVI / MVVM with Kotlin Coroutines & StateFlow
- **Build System**: Gradle with Kotlin DSL (`.kts`) & Version Catalog (`libs.versions.toml`)
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 35 (Android 15)
