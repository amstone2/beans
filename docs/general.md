# beans

Project: https://github.com/users/amstone2/projects/2/views/1
Repo: https://github.com/amstone2/beans

## Stack
- **Build tool:** Gradle — builds the project
- **Framework:** Kotlin Multiplatform (KMP) + Compose Multiplatform (UI)
- **Platforms:** iOS + Android
- **Co-developer:** Tanner (Linux, Android only)
- **Tooling:** Android Studio (both), Xcode (Alex only, required for iOS builds)

## Versions
- Kotlin: 2.3.21
- Compose Multiplatform: 1.10.3
- AGP: 9.2.0
- Gradle: 9.5.0
- compileSdk / targetSdk: 36 · minSdk: 24
- iOS deployment target: 16.0

## Dependencies
- **SQLDelight 2.3.2** — database (Android + iOS drivers, coroutines extension)
- **Voyager 1.0.1** — navigation (`voyager-navigator`, `voyager-tab-navigator`)
- **kmp-date-time-picker 1.1.1** — wheel date picker bottom sheet (`network.chaintech`)
- **materialIconsExtended** — Compose MP icon set

## Project Structure
```
composeApp/src/commonMain/   ← shared UI + logic (edit this most)
  kotlin/com/beans/
    App.kt                   ← root composable, TabNavigator + Scaffold
    AddBeanTab.kt            ← Add tab: bean entry form
    BeanListTab.kt           ← List tab: reactive bean list
    LocalBeansDatabase.kt    ← staticCompositionLocalOf<BeansDatabase>
    Platform.kt              ← expect fun currentDateString(): String
    db/
      Database.kt            ← createDatabase() factory
      DatabaseDriverFactory.kt ← expect class
  sqldelight/com/beans/db/
    Bean.sq                  ← Bean table + queries
    BeanFlavorTag.sq         ← BeanFlavorTag table + queries
    BeanRating.sq            ← BeanRating table + queries
composeApp/src/androidMain/  ← Android actuals
composeApp/src/iosMain/      ← iOS actuals + MainViewController
iosApp/                      ← Xcode project
```

## Architecture Patterns
- **Database access in screens:** `CompositionLocalProvider(LocalBeansDatabase provides database)` set in `App.kt`, consumed via `LocalBeansDatabase.current` in each tab
- **Reactive list:** `database.beanQueries.selectAll().asFlow().mapToList(Dispatchers.IO).collectAsState()`
- **Platform date:** `expect fun currentDateString(): String` — Android uses `SimpleDateFormat`, iOS uses `NSDateFormatter`

## Running the App
- **Android (Android Studio):** select emulator → Run button (use `androidApp` run config)
- **Android (CLI):** `ANDROID_HOME=~/Library/Android/sdk ./gradlew :androidApp:installDebug`
- **iOS:** `open iosApp/iosApp.xcodeproj` → select simulator → Run in Xcode

## Inspecting the Database (iOS Simulator)
```bash
find ~/Library/Developer/CoreSimulator -name "beans.db"
sqlite3 <path>
```
Path UUID changes on fresh installs. Android: use Android Studio → App Inspection → Database Inspector.

## TODOs
- [x] Decide on navigation library — chose Voyager
- [x] Implement Add Bean screen
- [x] Implement Bean List screen
- [x] Merge `adding-structure` branch into `main`

## Known Gotchas
- AGP 9.x is not compatible with combining `com.android.application` and the KMP plugin in one module. Fixed by splitting into two modules: `composeApp` uses `com.android.kotlin.multiplatform.library`, `androidApp` uses `com.android.application`
- `ANDROID_HOME` must be set: `export ANDROID_HOME=$HOME/Library/Android/sdk`
- iOS requires `CADisableMinimumFrameDurationOnPhone = true` in `Info.plist` — Compose enforces this for ProMotion displays. Using a manual `iosApp/iosApp/Info.plist` instead of Xcode's auto-generated one.
- All shared Compose UI must be wrapped in `Surface(modifier = Modifier.fillMaxSize())` or the screen renders black
- `iOSApp.swift` needs `.ignoresSafeArea(.all)` on `ContentView()`
- **Do not use `kotlinx-datetime` `Clock.System` on iOS** — it does not resolve with Kotlin 2.3.21's K2 compiler. Use `expect/actual`: `NSDateFormatter` on iOS, `SimpleDateFormat` on Android (see `Platform.kt`)
- **Use `Icons.AutoMirrored.Filled.List` not `Icons.Default.List`** — the latter is deprecated
- **Do not use `ExposedDropdownMenuBox`** — it is `@ExperimentalMaterial3Api`. Use radio buttons for small fixed option sets
