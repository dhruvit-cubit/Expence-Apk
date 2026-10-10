# Kharcha Khata (Android app)

Your expense, EMI, udhaar and lending tracker as an Android app. All data stays on the phone.

## Easiest way to get the APK (no Android Studio)

1. Make a free account at github.com and create a new **private** repository.
2. Upload everything from this folder into it (Add file > Upload files, drag the whole folder contents in, including the hidden `.github` folder).
3. Open the **Actions** tab, pick **Build APK**, press **Run workflow**. It takes about 5 minutes.
4. Open the finished run, download **KharchaKhata-apk** at the bottom, unzip it, and you have `app-debug.apk`.

## With Android Studio

Open this folder in Android Studio, wait for the sync, then Build > Build APK(s). The file lands in `app/build/outputs/apk/debug/`.

## Install on the phone

Copy `app-debug.apk` to the phone, open it, and allow "Install unknown apps" when asked. Later builds install over the old one and keep your data, because every build uses the same signing key. Take a backup (Settings > Copy backup) before you ever uninstall the app.

## Notes

- EMI alerts arrive as phone notifications at 9 AM. Allow notifications when the app asks.
- The page is in `app/src/main/assets/index.html`.
