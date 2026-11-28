# Amar Hisab 2.1 — Platform Guide

## Android
- Build the Android APK with the repository GitHub Actions workflow.
- The Android WebView uses the same app interface as the PWA.
- The profile picture chooser accepts image files; backup restore accepts JSON files.
- Local transaction data and profile picture remain on the device.

## iPhone and iPad (iOS)
- The `web/` folder is an installable Progressive Web App (PWA).
- Publish it over HTTPS (GitHub Pages is the planned free hosting option).
- Open the published address in Safari, choose Share, then Add to Home Screen.
- iOS uses the browser/PWA installation; this repository does not yet produce a native signed IPA.
- File exports and backup restore use the browser's supported share/download/file picker behavior.

## Desktop
- Open the same HTTPS PWA URL in a modern desktop browser.
- The layout adapts to desktop and mobile widths.
- The PWA can be installed from supported browsers.

## Data and privacy
- Data is stored locally in the browser/app storage by default.
- Browser storage is not automatically synchronized between Android, iOS, and desktop.
- Export a JSON backup on one device and restore it on another to transfer records.
- Profile photos are resized before being stored locally.
- Do not clear browser/app storage until a backup has been verified.

## Release checks still required
- Test the Android APK on a real Android phone, including image selection and JSON restore.
- Test the PWA in iOS Safari and desktop Chrome/Edge after HTTPS deployment.
- Verify CSV/XLS/PDF export and print on each target platform.
- Configure and preserve a release signing key before distributing APK updates.
