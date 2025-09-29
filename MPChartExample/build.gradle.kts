plugins {
    id("com.android.application")
}

android {
    namespace = "com.xxmassdeveloper.mpchartexample"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.xxmassdeveloper.mpchartexample"
        minSdk = 25
        targetSdk = 36
        versionCode = 57
        versionName = "3.1.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.work:work-runtime:2.10.5")
    implementation("com.google.android.material:material:1.13.0")
    implementation(project(":MPChartLib"))
}
