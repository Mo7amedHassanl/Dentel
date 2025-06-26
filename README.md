# Dentel - Dental Health App

## Overview

Dentel is a comprehensive dental health education and management app designed to help users maintain optimal oral hygiene and learn about dental procedures. The app provides informative articles, educational videos, and personalized features to promote better dental care.

## Features

- **Educational Content**: Access to a wide range of articles and videos about dental health topics
- **User Authentication**: Secure sign-in with email/password or Google account
- **Rich Media Support**: High-quality images and videos demonstrating proper dental care techniques
- **Favorites**: Save favorite articles and videos for quick access later
- **Personalized Profile**: Manage your account and preferences

## Technologies Used

- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern UI toolkit for building native Android UI
- **Firebase Authentication**: For secure user authentication
- **Cloud Firestore**: NoSQL cloud database for storing application data
- **Cloudinary**: For storing images and other media
- **Hilt**: For dependency injection
- **Coil**: Image loading library
- **Navigation Compose**: For handling in-app navigation
- **Markdown Rendering**: For rich text formatting in articles
- **Material Design 3**: For consistent and beautiful UI components

## Project Architecture

The application follows the MVVM (Model-View-ViewModel) architecture pattern:

- **Model**: Data layer that manages data retrieval and business logic
- **View**: UI layer built with Jetpack Compose
- **ViewModel**: Provides data to the UI and handles user interactions

## Getting Started

### Prerequisites

- Android Studio Arctic Fox (2021.3.1) or later
- JDK 11 or higher
- Android SDK 24 or higher

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Mo7amedHassanl/Dentel.git
   ```

2. Open the project in Android Studio.

3. Configure Firebase:
   - Create a Firebase project in the Firebase Console
   - Add an Android app to the Firebase project
   - Download the google-services.json file and place it in the app directory
   - Enable Authentication, Firestore, and Storage in the Firebase Console

4. Build and run the application on an emulator or physical device.

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/m7md7sn/dentel/
│   │   │   ├── app/                  # Application class
│   │   │   ├── data/                 # Data layer (repositories, models)
│   │   │   ├── presentation/         # UI layer (screens, components)
│   │   │   │   ├── common/           # Common UI components
│   │   │   │   ├── navigation/       # Navigation components
│   │   │   │   ├── theme/            # App theme
│   │   │   │   ├── ui/               # Screens and their components
│   │   │   │       ├── article/      # Article screen
│   │   │   │       ├── auth/         # Authentication screens
│   │   │   │       ├── home/         # Home screen
│   │   │   │       └── ...
│   │   │   └── utils/                # Utility functions and classes
│   │   │
│   │   ├── res/                      # Resources
│   │   └── AndroidManifest.xml       # App manifest
│   └── ...
└── ...
```

## Authentication

The app supports multiple authentication methods:
- Email/password authentication
- Google Sign-In
- (Facebook authentication coming soon)

## Rich Text and Images

Articles in the app support Markdown formatting, including:
- Headings of different levels
- Bold and italic text
- Lists (ordered and unordered)
- Links
- Images
- Code blocks
- Tables


## Acknowledgements

- AI was used to generate some of the content and code snippets in this project, including:
 - Refactoring code for better readability and maintainability
 - Adding comments and documentation
 - Generating example data for testing purposes
 - Assisting with UI design suggestions

