# Recharge.af Android app — version 1.1.0

The app opens https://recharge.af in Android WebView. Registration, top-up orders, customer balances, and support continue to use the live website. The native app provides the branded header and bottom navigation.

## Build on Windows with Android Studio
1. Extract this ZIP into a folder.
2. Open the folder in Android Studio and allow Gradle sync. Install Android SDK Platform 35 when prompted.
3. Select **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. Find the installable debug APK in `app/build/outputs/apk/debug/app-debug.apk`.
5. To update an already installed app, the new APK must be signed with the same key as the old installation. Debug APK signatures differ between build machines; if Android says it cannot update the app, uninstall the old debug app first. Uninstalling can remove the app's local session; your server account remains available for login.

## Branding
- `app/src/main/res/drawable/recharge_logo.png`: full horizontal logo in the native toolbar.
- `app/src/main/res/drawable/ic_launcher_foreground.png`: symbol for the adaptive launcher icon.
- `app/src/main/res/mipmap-*/ic_launcher.png`: launcher images for older Android versions.
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`: adaptive icon configuration.

Version 1.1.0 uses the same application ID (`af.recharge.app`) and website URL. The order and database logic remains on Recharge.af. This source package does not include a compiled APK.
