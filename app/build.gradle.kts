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
    kapt(Depends.lifecycleCompiler)
    kapt(Depends.roomCompiler)

    kaptAndroidTest(Depends.daggerCompiler)
    kaptAndroidTest(Depends.daggerProcessor)

    implementation(project(":presentation"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(Depends.kotlin)
    implementation(Depends.appcompat)
    implementation(Depends.lifecycle)
    implementation(Depends.constraintLayout)
    implementation(Depends.dagger)
    implementation(Depends.daggerAndroid)
    implementation(Depends.rxJava)
    implementation(Depends.room)
    implementation(Depends.roomRuntime)
    implementation(Depends.retrofit)
    implementation(Depends.retrofitRxJava2Adapter)
    implementation(Depends.retrofitGsonConverter)
    implementation(Depends.okhttpLoggingInterceptor)
    implementation(Depends.picasso)
    implementation(Depends.picassoDownloader)
    implementation(Depends.paging)
    implementation(Depends.pagingRx)

    testImplementation(Depends.junit)

    androidTestImplementation(Depends.testRunner)
    androidTestImplementation(Depends.testRules)
    androidTestImplementation(Depends.espressoCore)
    androidTestImplementation(Depends.mockito)
    androidTestImplementation(Depends.dexmaker)
}