# Wallpaper

An Android wallpaper discovery app. It searches the Pexels photo catalog, displays image collections, and lets users view, save, download, apply, and favorite wallpapers.

## Features

- Browse a set of suggested wallpapers and categories.
- Search for images using the Pexels API.
- View wallpaper images and apply them as the device wallpaper.
- Download images and save favorites.
- Cache search responses and favorite items locally with Room.
- Display ads with Google Mobile Ads.

## Tech stack

- Kotlin and Android Views with View Binding and Data Binding
- Retrofit and Gson for Pexels API requests
- Glide for image loading
- Room for local storage
- Koin dependency injection and AndroidX ViewModel
- Google Mobile Ads

## Requirements

- Android Studio with Android SDK 36 configured.
- JDK compatible with the included Gradle wrapper.
- Android 8.1 (API 27) or later to run the app.
- A Pexels API key for image search.

## Local configuration

Add the API key to the root `local.properties` file:

```properties
PEXELS_API_KEY=your_pexels_api_key
```

The release build also expects signing values from the local Gradle environment. Configure your own signing credentials locally and keep both the API key and signing information out of public source control.

## Build and run

Open this directory in Android Studio and sync Gradle, or build the debug APK from the project root:

```bash
./gradlew :app:assembleDebug
```

Install it on a connected device or emulator with:

```bash
./gradlew :app:installDebug
```

Internet access is needed to search and load images. Previously cached search responses and favorites are stored locally.
