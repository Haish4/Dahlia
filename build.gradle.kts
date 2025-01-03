buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        // Existing dependencies
        classpath(libs.gradle) // or the appropriate version for your project

        // Add this line for Firebase
        classpath(libs.google.services)  // For Firebase
    }
}




