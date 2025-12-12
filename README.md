# Home Assistant Kiosk App for Android 4.2

![Build APK](https://github.com/lukelloyd1985/HomeAssistantKiosk/workflows/Build%20APK/badge.svg)

A simple kiosk application that displays the Home Assistant web interface in fullscreen mode on Android 4.2 devices.

It can also be used for other purposes as the URL in kiosk is configurable.

## Features

- Fullscreen WebView displaying URL
- Kiosk mode with disabled navigation buttons
- Keep screen on
- Auto-launch on boot
- Landscape orientation
- JavaScript and DOM storage enabled for Home Assistant functionality

## Installation

Download from [Releases](https://github.com/lukelloyd1985/HomeAssistantKiosk/releases).

1. Enable "Unknown sources" in Android Settings > Security
2. Transfer the APK to your device
3. Install the APK
4. Set the app as the default launcher (Home app) when prompted

## Configuration

After installing the APK, you can configure the kiosk URL directly on the device:

1. Long-press the **top-right corner** of the screen for **3 seconds** and release
2. A settings dialog will appear
3. Enter your Home Assistant URL (e.g., `http://homeassistant.local:8123`)
4. Tap "Save"
5. Restart the app to load the new URL