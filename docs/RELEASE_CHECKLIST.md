# Amar Hisab — Release Checklist

Do not label a release final until the relevant checks below have been completed on a real Android device.

## Build and installation

- [ ] GitHub Actions build completed successfully.
- [ ] APK downloaded from the correct commit/branch.
- [ ] Version name, version code and application ID checked.
- [ ] Fresh install tested.
- [ ] Existing old app remains installed and its data remains untouched.
- [ ] App opens and language selection works.

## Records and reports

- [ ] Add income and verify dashboard totals.
- [ ] Add expense and verify balance.
- [ ] Check transaction list, date filtering and monthly report.
- [ ] Check large amounts, Bengali text and long descriptions.
- [ ] CSV download/save tested and opened in a spreadsheet app.
- [ ] Excel-compatible export tested and opened in a spreadsheet app.
- [ ] PDF print preview opens; Save as PDF and physical printing tested where available.

## Backup and restore

- [ ] Create a JSON backup using the Android file picker.
- [ ] Confirm the saved file exists and has non-zero size.
- [ ] Make a duplicate of the test backup.
- [ ] Restore from the duplicate file.
- [ ] Confirm all records, totals, categories and settings are restored.
- [ ] Invalid or damaged backup is rejected safely.
- [ ] Canceling the picker does not change data.
- [ ] Restoring does not silently erase existing records.
- [ ] Backup/restore behavior is documented for users.

## Release and GitHub preservation

- [ ] All source and configuration changes committed to GitHub.
- [ ] README and handover notes updated.
- [ ] Build artifact downloaded and tested.
- [ ] Permanent versioned GitHub Release created with tested APK attached.
- [ ] Release notes explain whether this is a separate app or an update.
- [ ] No private signing keys, passwords, tokens or personal backups committed.
- [ ] User has independently confirmed the backup and report features work.
