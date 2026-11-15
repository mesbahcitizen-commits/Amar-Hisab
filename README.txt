Amar Hisab — Offline Android App
================================
This corrected build includes:
- Working Edit form: change transaction type, date, amount, category and note, then Save Changes.
- Delete confirmation.
- Bengali and English language selector; the selected language is saved on the device.
- User-provided portrait-based Amar Hisab launcher icon at all Android densities.
- Local offline storage, CSV export, and JSON backup/restore.

IMPORTANT FIX:
The previous JavaScript stopped during startup because the translation table called tr() before currentLang/translations were initialized, and the DOM helper was declared after its first use. Both startup errors are corrected.

Build:
GitHub Actions → Build Amar Hisab APK. After a successful run, download the Amar-Hisab-APK artifact.
