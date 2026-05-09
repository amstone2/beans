# Setup

## Required Environment Variables

| Variable | Alex (macOS) | Tanner (Linux) |
|----------|-------------|----------------|
| `ANDROID_HOME` | `$HOME/Library/Android/sdk` | `$HOME/Android/Sdk` |

---

## Stack
- **Kotlin Multiplatform** — shared code across iOS and Android
- **Compose Multiplatform** — shared UI
- **Kotlin 2.3.21** · **Compose 1.10.3** · **AGP 9.2.0** · **Gradle 9.5.0**

---

## Alex (macOS) — iOS + Android

1. **Install Android Studio** — https://developer.android.com/studio
2. **Install Xcode** from the Mac App Store
3. **Install the KMP plugin** in Android Studio
   - Preferences → Plugins → search "Kotlin Multiplatform Mobile" → Install → Restart
4. **Add `ANDROID_HOME` to your shell profile** (`~/.zprofile`):
   ```bash
   export ANDROID_HOME=$HOME/Library/Android/sdk
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   ```
5. **Add your Java home to `local.properties`** in the repo root:
   ```
   org.gradle.java.home=/Users/alex/Library/Java/JavaVirtualMachines/openjdk-21.0.1/Contents/Home
   ```
   Run `java -XshowSettings:property -version 2>&1 | grep java.home` to find your path.
6. **Create an Android emulator** in Android Studio → Device Manager → + → Create Virtual Device
7. **Open the project** in Android Studio → File → Open → select the repo root
8. Let Gradle sync finish, then hit Run to launch on Android
9. **For iOS** — open the Xcode project and select a simulator, then run:
   ```bash
   open iosApp/iosApp.xcodeproj
   ```

---

## Tanner (Linux) — Android only

> iOS builds require macOS — Alex handles those.

1. **Install Android Studio** — https://developer.android.com/studio
2. **Install the KMP plugin** in Android Studio
   - Preferences → Plugins → search "Kotlin Multiplatform Mobile" → Install → Restart
3. **Add `ANDROID_HOME` to your shell profile** (`~/.bashrc` or `~/.zshrc`):
   ```bash
   export ANDROID_HOME=$HOME/Android/Sdk
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   ```
4. **Add your Java home to `local.properties`** in the repo root:
   ```
   org.gradle.java.home=/home/tanner/.jdks/openjdk-21/Contents/Home
   ```
   Run `java -XshowSettings:property -version 2>&1 | grep java.home` to find your path.
5. **Create an Android emulator** in Android Studio → Device Manager → + → Create Virtual Device
6. **Open the project** in Android Studio → File → Open → select the repo root
7. Let Gradle sync finish, then hit Run to launch on Android

---

## Running on Android (command line)

Make sure you have an emulator running first, then:

```bash
ANDROID_HOME=~/Library/Android/sdk ./gradlew :androidApp:installDebug
```

> **Tanner:** replace `~/Library/Android/sdk` with `~/Android/Sdk`

### First-time troubleshooting

If you see a `jlink` error referencing a VS Code or IDE JRE path, Gradle picked up the wrong JVM. Fix it by stopping all daemons and clearing the transforms cache:

```bash
./gradlew --stop
find ~/.gradle/caches -name "transforms-*" -maxdepth 1 -type d -exec rm -rf {} +
```

Then run the install command again. This is a one-time issue — `local.properties` pins Gradle to the correct JDK going forward.

---

## Project Structure

```
androidApp/           ← Android app entry point (MainActivity)
composeApp/
  src/commonMain/     ← shared UI and logic (edit this most of the time)
  src/androidMain/    ← Android-specific shared code
  src/iosMain/        ← iOS entry point
iosApp/               ← Xcode project (iOS shell app)
```

Most of your day-to-day code lives in `composeApp/src/commonMain/`.
