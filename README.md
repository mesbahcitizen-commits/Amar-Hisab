# Amar Hisab (আমার হিসাব)

A personal income and expense tracker for Android. The Android app uses a WebView interface backed by local browser storage.

## Current development version

- App display name: আমার হিসাব (নতুন)
- Version name: 1.2
- Version code: 3
- Android application ID: `com.example.amarhisab.export`
- Android namespace: `com.example.amarhisab`
- Minimum Android SDK: 23
- Compile / target SDK: 35
- Java: 17
- Gradle: 8.9

> This is a separate-install test build. It does not automatically import data from the older Amar Hisab app. Keep the old app installed until export/import has been tested successfully.

## Repository layout

- `app/src/main/assets/index.html` — application interface and report/export logic.
- `app/src/main/java/com/example/amarhisab/MainActivity.java` — Android WebView, file save/restore picker, and Android print bridge.
- `app/src/main/AndroidManifest.xml` — Android app manifest.
- `app/build.gradle` — Android app ID, SDK and version settings.
- `.github/workflows/build-apk.yml` — GitHub Actions APK build.
- `docs/PROJECT_HANDOVER.md` — project recovery, build, backup and release notes.
- `docs/RELEASE_CHECKLIST.md` — checks required before public distribution.

## Build an APK on GitHub

1. Open the repository's **Actions** tab.
2. Select **Build Amar Hisab APK**.
3. Choose **Run workflow** and select the branch to build.
4. Wait for the workflow to finish successfully.
5. Open the completed run and download the `Amar-Hisab-APK` artifact.
6. Extract the ZIP to obtain `app-debug.apk`.

You can also build locally with JDK 17, Android SDK (platform 35/build tools 35.0.0), and Gradle 8.9:

```bash
gradle assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## Data safety

- The app's browser data is stored locally on the device.
- Use the in-app Backup option and save the JSON file somewhere safe.
- Test Restore with a copy of a backup file before relying on it.
- Never clear app storage or uninstall an app that contains the only copy of important records.
- A new package ID installs separately and does not migrate old app data automatically.

## Important repository rules

- Commit all source code, build configuration, documentation, and non-secret resources to GitHub.
- Do not keep the only copy of an important file in a temporary Actions artifact or a ChatGPT download.
- Do not commit passwords, signing keys, keystores, API tokens, or personal backup data.
- Keep release APKs in GitHub Releases when a permanent downloadable build is needed; Actions artifacts are temporary.
- Before changing application ID, local-storage keys, or data format, document a migration plan and test backup/restore.

See [Project Handover](docs/PROJECT_HANDOVER.md) before continuing development.
