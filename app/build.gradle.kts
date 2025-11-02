plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.chandra.practice.notesmvvm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.chandra.practice.notesmvvm"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt") ,
                    "proguard-rules.pro"
                         )
        }
        debug {
             isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt") ,
                    "proguard-rules.pro"
                         )
        }
    }
    viewBinding.enable = true
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.desktop)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.core.i18n)
    implementation(libs.androidx.preference)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Room components
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler) // Use kapt for annotation processing
    implementation(libs.androidx.room.ktx) // Add this line

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Lifecycle components
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    //Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    // Play Services Location for FusedLocationProviderClient
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // Kotlin Coroutines (optional, for background tasks)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ---------------------------UI Testing--------------------
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.4.0-alpha06")

}