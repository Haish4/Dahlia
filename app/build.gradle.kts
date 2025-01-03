
dependencies {
    // Core Libraries
    implementation(libs.androidx.core.ktx.v1100)
    implementation(libs.androidx.appcompat)
    implementation(libs.material.v190)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)

    // Navigation dependencies

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Google Play Services for Location API
    implementation(libs.play.services.location) // Latest version

    // Google Maps SDK
    implementation(libs.play.services.maps) // Latest version

    // Firebase dependencies
    implementation(libs.firebase.database) // Firebase Realtime Database
    implementation(libs.firebase.auth) // Firebase Authentication (if needed for user login)

    // Firebase Cloud Messaging (Optional for push notifications)
    implementation(libs.firebase.messaging) // Latest version

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
}







