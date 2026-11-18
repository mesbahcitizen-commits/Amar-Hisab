# Android signing and release continuity

## Current v1.3 test build

- Application ID: `com.example.amarhisab.export` (intentionally separate from the legacy app so both can be installed side by side).
- Version: `1.3` (versionCode 4).
- Current CI artifact is a **debug-signed test APK**. It is suitable for device testing, not a public Play Store release.
- GitHub Actions artifacts expire. The current artifact is retained by GitHub for 90 days; use a tagged release asset for durable distribution.

## Important signing-key rule

Android only permits an in-place app update when the new APK is signed with the same signing certificate as the installed APK. The old legacy signing key is unavailable, so v1.3 uses a new application ID and can be installed alongside the old app.

Do not change the application ID after users install v1.3. Also, do not distribute successive CI debug APKs as updates: GitHub-hosted runners generate a fresh debug keystore, so debug signatures may differ between builds. Users may otherwise need to uninstall the app, which deletes its local app data.

## Before public distribution or v1.4 upgrades

1. Generate a dedicated release keystore on a trusted computer and keep a secure offline copy.
2. Never commit the keystore or passwords to this repository.
3. Add the following repository Actions secrets:
   - `ANDROID_KEYSTORE_BASE64`
   - `ANDROID_KEYSTORE_PASSWORD`
   - `ANDROID_KEY_ALIAS`
   - `ANDROID_KEY_PASSWORD`
4. Configure the release build to sign with that keystore and verify the certificate fingerprint.
5. Keep the same keystore, alias, and application ID for every future update.
6. Only create a public GitHub Release after device tests pass and the signed release APK has been verified.

## v1.3 device acceptance tests

- Install v1.3 alongside the legacy app; confirm neither app overwrites the other.
- Add sample income and expense entries; close and reopen the app and confirm data persists.
- Export a JSON backup through Android's save-file picker.
- Restore the JSON backup into a clean v1.3 install and verify totals and transaction details.
- Restore a legacy-format JSON backup if one can be obtained from the old app; compare totals and record counts.
- Export CSV and Excel-compatible XLS for a selected month; open both files.
- Print the selected-month PDF and complete-history PDF; inspect Bengali text, totals, page breaks, and A4 layout.
- Do not erase the old app or its data until the user's real records have been safely recovered and verified.
