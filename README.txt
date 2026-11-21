Amar Hisab 2.1 — Personal Finance App
====================================

Platform targets:
- Android APK (WebView wrapper)
- iOS Safari installable Progressive Web App (PWA)
- Desktop browser / installable PWA

v2.1 updates:
- Profile photo selection in the top-left header; photo is resized and stored locally.
- Market subcategories: chicken, beef, fish, fresh vegetables, rice, cooking oil.
- Medical subcategories: doctor visit, tests, medicine.
- Transaction history shows income first, grouped in Salary, Business, Extra Income, Gift order; then expenses, both chronological from earliest to latest.
- Dates are displayed as DD-MM-YYYY in transaction history and exports.
- Responsive mobile and desktop layout.
- PWA files are in the web/ directory for HTTPS hosting; service worker enables offline use after first visit.

Data:
- Financial data remains local to each device/browser.
- Use JSON backup/restore to transfer data between devices; data is not automatically synchronized.
- Keep a verified backup before uninstalling or clearing app/browser data.

Android build:
GitHub Actions → Build Amar Hisab APK. Download the Amar-Hisab-APK artifact after a successful build.

iOS and desktop:
Publish the web/ directory over HTTPS (GitHub Pages is the planned hosting option). On iPhone/iPad, open the published site in Safari → Share → Add to Home Screen. This is a PWA, not a signed native iOS IPA.

Before public release:
- Test profile image selection on Android and iOS.
- Test old JSON backup restore, CSV/XLS export and PDF printing.
- Preserve a dedicated Android release signing key before distributing updates.
