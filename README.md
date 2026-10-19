# NailStudio Android App

Native Android app built with Kotlin and XML layouts.

## Open locally
1. Extract the project folder.
2. Open the folder containing `settings.gradle.kts` in Android Studio.
3. Use JDK 17, sync Gradle, then run the `app` configuration on an Android device.
4. Build from a terminal with Gradle 9.1 installed: `gradle assembleDebug` and `gradle testDebugUnitTest`.

## GitHub CI
The workflow at `.github/workflows/android-ci.yml` builds a debug APK and runs JVM unit tests on pushes and pull requests to `main` and `develop`. Follow `devops/GITHUB_AND_DEVOPS_GUIDE.md` to publish the repository and inspect CI results.

## Database reference
i Used a backend API between Android and a hosted database.

