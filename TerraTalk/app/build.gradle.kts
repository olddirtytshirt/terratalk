plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

    //google maps gradle plugin
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    //keep namespace the same as applicationId below.
    //should always match the project's base package name (app code)
    //DO NOT CHANGE
    namespace = "com.example.terratalk"
    compileSdk = 34

    defaultConfig {
        //DO NOT CHANGE
        applicationId = "com.example.terratalk"
        //min api level
        minSdk = 26
        //target api level
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        //turn jetpack compose on
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


    secrets {
        // Optionally specify a different file name containing your secrets.
        // The plugin defaults to "local.properties"
        propertiesFileName = "secrets.properties"

        // A properties file containing default secret values. This file can be
        // checked in version control.
        defaultPropertiesFileName = "local.defaults.properties"

        // Configure which keys should be ignored by the plugin by providing regular expressions.
        // "sdk.dir" is ignored by default.
        ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
        ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
    }
}

dependencies {

    //firebase imports
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")

    //firebase database
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth-ktx")


    //android compose
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.1")
    implementation("androidx.compose.material:material-icons-extended:1.6.1")


    implementation("androidx.core:core-ktx")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx")
    implementation("androidx.activity:activity-compose")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.1")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")

    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("androidx.room:room-ktx:2.6.1")

    //need this or MapEffect throws exception.
    implementation("androidx.appcompat:appcompat:1.6.1")

    //google maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.google.maps:google-maps-services:0.15.0")
    //google maps for compose
    implementation("com.google.maps.android:maps-compose:2.8.0")

    //ktx for the Maps SDK for Android
    implementation("com.google.maps.android:maps-ktx:3.2.1")
    //ktx for the Maps SDK for Android Utility Library
    implementation("com.google.maps.android:maps-utils-ktx:3.2.1")

    //hilt
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation ("com.google.android.libraries.places:places:3.3.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")

    //retrofit - REST backend
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // CONVERTER FOR RETROFIT (GSON)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //retrofit - convert JSON output to string
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


}