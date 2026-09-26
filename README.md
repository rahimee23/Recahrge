# Recharge.af Android app

A native Android shell around the existing HTTPS Recharge.af customer website. Registration, sign-in, wallet balance, operator detection, custom airtime and order creation all use the existing website and database. No API credentials are stored in the app. Website updates appear automatically in the app.

## Build an APK

Open this folder in Android Studio (JDK 17, Android SDK 35). Let Gradle sync, then choose **Build > Build APK(s)**. The debug APK appears at `app/build/outputs/apk/debug/app-debug.apk`. For public distribution, use **Build > Generate Signed Bundle / APK** and a private release signing key.

Android Gradle Plugin 8.7.3 and Gradle 8.9 are suitable. If Android Studio asks for a Gradle wrapper, create one via the IDE or an installed Gradle 8.9 runtime. Keep the app package `af.recharge.app` unless you intentionally choose a different Play Store ID.

The app needs internet and Android System WebView. It only loads HTTPS pages from recharge.af inside the app; WhatsApp links open externally. Customer sessions are stored as normal website cookies. The native bottom tabs open Home, Orders, Balance and Help.

This workspace had no Android SDK/Gradle, so no compiled APK or device test was possible here. Before distribution, build in Android Studio and test registration, login, wallet, custom top-up, back navigation, and WhatsApp on a device. Use a separate test user and avoid real supplier deductions during app QA.

## Codemagic option

If you already use Codemagic, put this project in a Git repository and import it there. The included `codemagic.yaml` runs `gradle :app:assembleDebug` and publishes the debug APK as an artifact. A debug APK is for private testing; sign a release APK or AAB for distribution.
