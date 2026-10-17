# Nail Studio Android App — Reliability Fixes

This is a Kotlin/XML Android Studio project based on the supplied source ZIP.

## Open and run
1. Extract the ZIP.
2. In Android Studio choose **Open** and select the folder containing `settings.gradle.kts`.
3. Allow Gradle Sync to finish, then run the app on an emulator or Android device.
4. If the build fails, use the first error in the Build output to diagnose it.

## Changes in this revision
- Login now requires an account created through Register and verifies the password hash; registration validates name/email and requires at least 8 password characters.
- Booking validates date/time, phone number, salon opening hours (09:00–18:00), service duration, and conflicts with existing confirmed bookings.
- Date picker prevents choosing past dates; choosing a new date clears the previously selected time.
- Booking JSON reads handle invalid saved data without crashing, and My Bookings refreshes when resumed.
- Contact messages are saved locally and the UI clearly states they have not been emailed or sent to the salon.
- About and Contact screens are reachable from Home.
- Added the referenced ProGuard rules file for release build configuration.

## Important limitations
- Authentication and bookings are stored locally on one device. This is a demonstration implementation, not a production account system or shared salon booking backend.
- Passwords are salted and hashed, but production apps should use a reputable authentication provider rather than custom local authentication.
- Appointment conflict checking only sees bookings stored on this device. A backend/database is required to prevent conflicts between different customers' devices.
- The project has not been compiled or executed in this environment because Gradle/Android SDK tooling is unavailable here. Please build it in Android Studio before deploying it.
