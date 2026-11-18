# Amar Hisab — Project Handover

Last updated: 2026-11-18

## 1. Source of truth

Repository: https://github.com/mesbahcitizen-commits/Amar-Hisab

Current working branch: `feature/old-app-export`

Draft pull request: https://github.com/mesbahcitizen-commits/Amar-Hisab/pull/1

Do not assume the feature branch has been merged into `main`. Continue work on the feature branch unless the project owner explicitly approves merging.

## 2. Essential files that must remain in GitHub

The following are required to continue development and rebuild the Android app:

| File | Purpose |
|---|---|
| `app/src/main/assets/index.html` | App screens, data handling, income/expense records and export interface |
| `app/src/main/java/com/example/amarhisab/MainActivity.java` | WebView host, Android document picker, save-file bridge and print bridge |
| `app/src/main/AndroidManifest.xml` | Android app manifest |
| `app/build.gradle` | Application ID, SDK and app version |
| `build.gradle` | Root Gradle configuration |
| `settings.gradle` | Gradle project settings |
| `.github/workflows/build-apk.yml` | Automated APK build |
| `README.md` | Project overview and build steps |
| `docs/PROJECT_HANDOVER.md` | This handover document |
| `docs/RELEASE_CHECKLIST.md` | Pre-release testing checklist |
| `app/src/main/res/` | App icons and Android resources |

Do not delete or rename these files without first updating the build instructions and testing a fresh build.

## 3. Current Android configuration

- Version name: `1.3`
- Version code: `4`
- Application ID: `com.example.amarhisab.export`
- Namespace: `com.example.amarhisab`
- minSdk: 23
- compileSdk / targetSdk: 35
- Java: 17
- Gradle: 8.9

The separate application ID means this build installs alongside the earlier app. It does not by itself copy old app data.

## 4. Build procedure

GitHub Actions workflow: `.github/workflows/build-apk.yml`

1. Push/commit source changes to the working branch.
2. Open Actions → Build Amar Hisab APK.
3. Run the workflow, or let the push trigger it.
4. Confirm the workflow is successful.
5. Download the `Amar-Hisab-APK` artifact from that run.
6. Extract the ZIP and test the APK on an Android device.

Local build command:

```bash
gradle assembleDebug
```

Output:

```
app/build/outputs/apk/debug/app-debug.apk
```

Actions artifacts are temporary and expire according to the workflow/repository retention policy. For a permanent public download, create a versioned GitHub Release and attach the tested APK. Keep the source and workflow in GitHub even when a release is attached.

## 5. Backup, restore and print implementation notes

Android host code is in `MainActivity.java`:

- JavaScript bridge name: `AndroidBridge`
- Save operation: `AndroidBridge.saveFile(name, mime, base64)`
- Save uses Android's `ACTION_CREATE_DOCUMENT` picker, so the user chooses the destination.
- Restore opens Android's `ACTION_OPEN_DOCUMENT` picker for `application/json`.
- Print operation: `AndroidBridge.printReport(html)`, which uses Android `PrintManager` and A4 print attributes.

The interface and report logic are in `index.html`. Check that the JavaScript actually calls these bridge methods and that the backup format contains all records and settings before declaring Backup/Restore complete.

Important: a build succeeding only proves that the APK compiled. It does not prove that backup, restore, PDF printing, CSV or Excel exports work on a real phone. Test each feature on-device and record the result.

### v1.3 behavior

- JSON backup format version 2 includes app identifier, creation timestamp, language preference, and all transaction records.
- Restore accepts the earlier v1 backup envelope as well as v2, validates all transaction records, limits selected backup size to 20 MB, and merges records by ID to avoid duplicates.
- CSV and Excel-compatible XLS exports use the selected month.
- PDF/Print offers a selected-month report and a separate complete-history report, with A4 print CSS and repeated table headings.
- Local date defaults use the device's local timezone rather than UTC.

## 6. Data protection rules

1. Never ask the user to uninstall the old app or clear its storage as a troubleshooting step.
2. Do not overwrite existing data during restore without a clear confirmation and a backup-first warning.
3. Validate JSON backup structure before importing it.
4. Prefer a restore preview or confirmation, and preserve existing records if a file is invalid.
5. Test restore using a duplicate/test backup, not the only backup.
6. Verify that a saved backup file can be opened and restored.
7. Verify PDF print preview and saving to PDF; verify CSV and Excel files open correctly.
8. Keep the original app and its data untouched until the user has independently verified the migrated records.

## 7. Signing credentials and secrets

No repository or environment Actions secrets were visible in the repository Secrets page on 2026-11-18. Do not assume the old signing key exists.

Never commit signing private keys, passwords, tokens, or keystore files to Git. If a future Play Store release needs a signing setup, configure the required secrets securely in GitHub and document only the secret *names* and setup process, never their values.

Debug signing keys may differ between build environments. Do not promise that a debug APK can update another debug APK unless the signing certificate has been compared.

## 8. Required handover rule

Every future code change must be committed to this GitHub repository. Update this handover document whenever the app ID, version, build process, backup format, storage key, signing setup, or release process changes. Keep a tested release APK in GitHub Releases for long-term access, not only in temporary Actions artifacts.
