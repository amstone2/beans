# beans

A coffee bean tracker. Logs beans you own, brews you've pulled, and ratings from each taster. Built as a Kotlin Multiplatform app running on Android and iOS, with shared UI via Compose Multiplatform.

## Stack

Kotlin 2.3.21 · Compose Multiplatform 1.10.3 · SQLDelight 2.3.2 · AGP 9.2.0 · Gradle 9.5.0

## Quick start

**Android** (with an emulator running):

```bash
./gradlew :composeApp:installDebug
```

**iOS** (macOS only):

```bash
open iosApp/iosApp.xcodeproj
```

Then hit Run in Xcode.

Most day-to-day code lives in `composeApp/src/commonMain/`.

## Docs

- [Setup](docs/setup.md) — environment variables, IDE setup, troubleshooting
- [Database](docs/database.md) — SQLDelight schema reference
- [General](docs/general.md) — stack details and project links
- [Kotlin cheatsheet](docs/kotlin-cheatsheet.md)
- [TODO](docs/TODO.md) — open work
