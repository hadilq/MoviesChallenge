plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.compileSdk)
    defaultConfig {
        applicationId = "com.github.hadilq.movieschallenge"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        setTargetCompatibility(1.8)
        setSourceCompatibility(1.8)
    }
}

dependencies {
    kapt(Depends.daggerCompiler)
    kapt(Depends.daggerProcessor)
    kapt(Depends.archComponentsCompiler)

    implementation(Depends.kotlin)
    implementation(Depends.appcompat)
    implementation(Depends.coreKtx)
    implementation(Depends.constraintLayout)

    testImplementation(Depends.junit)

    androidTestImplementation(Depends.testRunner)
    androidTestImplementation(Depends.testRules)
    androidTestImplementation(Depends.espressoCore)
}