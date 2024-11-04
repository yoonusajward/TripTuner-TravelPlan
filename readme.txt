
TripTuner - Travel Planning Application Using GPT Model

TripTuner is a Kotlin-based Android application designed to help users generate personalised travel itineraries. The app allows users to input a destination and, with the help of a GPT model, generates an itinerary for the location. The app also allows users to manually add budget amount, companions, and other preferences, and saves the final itinerary for future reference.

This project has been developed by Mohammed Ajward Yoonus.

Tools and Applications Required
-------------------------------
To run and test this project, you will need the following tools:

1. Android Studio
- Version: Latest stable version
- Purpose: Android Studio is the Integrated Development Environment (IDE) used for developing this Android application in Kotlin.
- Download: https://developer.android.com/studio

2. Kotlin Language
- The project has been developed using the Kotlin programming language.
- Android Studio comes with built-in support for Kotlin, so no separate installation is required.

3. Virtual Device
- Virtual Device Used: Pixel 4 API 28 (Android 9.0)
- Purpose: The app has been tested and developed using this virtual device. If you do not have an actual device, use the Android Emulator in Android Studio with the same virtual device for testing.


Project Structure

The project is organized into the following main components:

├── model/                # Data models
├── ui/                   # UI components (Fragments)
├── network/              # Repository handling API requests to the GPT model
├── viewmodel/            # ViewModel classes handling logic
├── res/                  # Resources (layouts, drawables, strings)
└── MainActivity.kt       # Main activity managing navigation

Installation and Setup Instructions

1. Copy the Project Files
- Copy the project files from the DVD into a local folder on your system.

2. Open the Project in Android Studio
- Launch Android Studio.
- Select Open an existing project and navigate to the location where the project files are stored.
- Sync Gradle files when prompted.

3. Run the Application
- Connect an Android device or use the Pixel 4 API 28 virtual device in Android Studio.
- Press the Run button in Android Studio to launch the application.

Features of TripTuner

- User Authentication: Sign-up and login using Firebase Authentication.
- Itinerary Generation: Enter a destination and get the information of the specific destination location using the GPT model.
- Itinerary Management: View, edit, delete and save itineraries.
- Custom Inputs: Users can add details such as budget, companions and special notes.
- Budget Planner: Set a budget, log expenses, and get notified if you exceed it.

Common Issues and Error Handling

1. Firebase Authentication Issues
- Error: Login or sign-up failure.
- Solution: Ensure that the email and password are valid. Error messages will be displayed via Toast notifications.

2. API Errors
- Error: The GPT model fails to generate an itinerary.
- Solution:
	- Check the network connection and verify that the GPT model API is properly hosted and available.
	- If a timeout error occurs due to an API delay, navigate away from the "Create Itinerary" screen and return to retry the request.

If you have any questions or face issues while running the project, feel free to contact me.

Contact: [yoonusajward27@gmail.com]
