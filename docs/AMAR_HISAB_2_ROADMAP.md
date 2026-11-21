# Amar Hisab 2.0 — Development Plan

## Goal
Prepare a tested, free personal-finance app release for APK distribution and Google Play.

## Preserve the current release
- Keep the existing `main` branch as the stable version.
- Develop 2.0 on `feature/amar-hisab-2.0`.
- Preserve the existing Android application ID (`com.example.amarhisab`) unless a deliberate migration is approved.
- Do not overwrite user records during upgrades. Current records are stored locally in the WebView browser storage; backup/restore compatibility must be tested before release.
- Verify signing-key continuity before promising seamless APK upgrades. The current workflow builds a debug APK; Play Store publishing requires a properly signed release App Bundle.

## v2.1 changes in progress
- Profile photo upload and local persistence in the top-left app header.
- Nested expense categories for Market and Medical.
- Day-month-year date display in transaction history and exports.
- Income-first transaction history; income grouped by category and date ascending, followed by expenses in date-ascending order.
- Responsive web interface for mobile and desktop, with an installable offline PWA for iOS Safari and desktop browsers.

## Planned modules

### 1. Accounts and transfers
- Cash, multiple bank accounts, bKash wallets, and Nagad wallets.
- Opening balance, account name, optional account identifier, and current balance.
- Deposits and withdrawals for each account.
- Transfers between Cash, Bank, bKash, and Nagad.
- Transfers must not be counted as income or expense.
- Optional transaction reference and note; do not connect to financial accounts or request PIN/OTP.

### 2. Loans and EMI
- Loan received with lender, purpose, principal, date, interest terms, and notes.
- Repayment schedule and repayment history.
- Separate principal and interest portions.
- Outstanding principal and next due date.
- EMI plans, paid/remaining installments, and reminders where supported.
- Loan proceeds are liabilities, not income; principal repayment reduces liability; interest is an expense.

### 3. Income and expenses
- Built-in and user-defined categories.
- Add, edit, delete, search, and filter transactions.
- Daily, weekly, monthly, and yearly summaries.
- Charts and account-wise reports.

### 4. Export, backup, and preferences
- CSV plus properly formatted PDF and Excel exports.
- Backup and restore with validation and duplicate handling.
- Bengali and English language support.
- Light and dark themes.
- PIN lock and currency/date preferences.

## Release acceptance checks
- Existing backup files can be restored.
- Upgrade tests confirm records remain intact.
- Transfers do not inflate income/expense totals.
- Loan principal and interest are accounted for correctly.
- Bank, bKash, and Nagad balances reconcile with transaction history.
- Test on supported Android versions and screen sizes.
- Produce and test a signed APK and Android App Bundle (AAB).
- Complete Google Play policy, testing, and store-listing requirements before publication.

## Privacy and security
- Store financial records locally by default.
- Never ask users for bank, bKash, or Nagad PINs, passwords, or OTPs.
- Clearly explain backup location and restore behavior.
- Do not claim automatic bank or wallet integration; entries are manual unless a compliant integration is separately implemented.
