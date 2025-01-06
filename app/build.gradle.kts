plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.dahlia"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dahlia"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    implementation("androidx.cardview:cardview:1.0.0")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Scalable Size Unit
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)


    //rounded iv
    implementation("com.makeramen:roundedimageview:2.3.0")

    //Firebase
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.firestore)
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    //MultiDex
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.github.bumptech.glide:glide:4.15.0")

    implementation (libs.play.services.maps)
    implementation (libs.play.services.location)

    implementation (libs.room.runtime)
    annotationProcessor (libs.room.compiler)


    //RetroFit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

}