# Home Assistant Kiosk App for Android 4.2.2

![Build APK](https://github.com/USERNAME/REPO-NAME/workflows/Build%20APK/badge.svg)

A simple kiosk application that displays the Home Assistant web interface in fullscreen mode on Android 4.2.2 devices.

## Features

- Fullscreen WebView displaying Home Assistant
- Kiosk mode with disabled navigation buttons
- Keep screen on
- Auto-launch on boot
- Landscape orientation
- JavaScript and DOM storage enabled for Home Assistant functionality

## Configuration

Before building, update the Home Assistant URL in [MainActivity.java](HomeAssistantKiosk/app/src/main/java/com/homeassistant/kiosk/MainActivity.java:15):

```java
private static final String HOME_ASSISTANT_URL = "http://homeassistant.local:8123";
```

Replace with your actual Home Assistant URL (e.g., `http://192.168.1.100:8123`).

## Building the App

### Option 1: Download Pre-built APK (Recommended)

The APK is automatically built using GitHub Actions on every commit. You can download the latest build:

1. Go to the [Actions tab](https://github.com/USERNAME/REPO-NAME/actions) in the repository
2. Click on the latest successful workflow run
3. Download the APK artifact from the "Artifacts" section
4. Extract the zip file to get the APK

**Or** wait for a tagged release and download from the [Releases page](https://github.com/USERNAME/REPO-NAME/releases).

### Option 2: Build Locally

#### Prerequisites

- Android SDK with API Level 17 (Android 4.2.2)
- Gradle
- JDK 7 or 8

#### Build Steps

1. Open a terminal in the `HomeAssistantKiosk` directory
2. Run: `./gradlew assembleRelease` (Linux/Mac) or `gradlew.bat assembleRelease` (Windows)
3. The APK will be generated in `app/build/outputs/apk/`

Or import the project into Android Studio and build from there.

## Installation

1. Enable "Unknown sources" in Android Settings > Security
2. Transfer the APK to your device
3. Install the APK
4. Set the app as the default launcher (Home app) when prompted

## Kiosk Mode Setup

1. Go to Settings > Home
2. Select "Home Assistant Kiosk" as the default launcher
3. The app will now launch automatically and act as the home screen

## Exiting Kiosk Mode

To exit kiosk mode and access device settings:
- You may need to clear the app as the default launcher from Android Settings
- Consider implementing a secret gesture or hidden button for easier access

## Notes

- The app requires internet access to load Home Assistant
- Ensure your Android device is on the same network as your Home Assistant instance
- The back button is disabled in the app
- For true kiosk mode, you may want to use Android's Screen Pinning feature (if available on your device)

## Icon

The project requires launcher icons. You'll need to add `ic_launcher.png` files to these directories:
- `app/src/main/res/mipmap-mdpi/` (48x48)
- `app/src/main/res/mipmap-hdpi/` (72x72)
- `app/src/main/res/mipmap-xhdpi/` (96x96)
- `app/src/main/res/mipmap-xxhdpi/` (144x144)

You can generate icons using Android Asset Studio or use any Home Assistant logo.
