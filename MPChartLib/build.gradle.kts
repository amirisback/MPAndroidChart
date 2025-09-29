import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.github.mikephil.charting"
    compileSdk = 36

    defaultConfig {
        minSdk = 25
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

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.9.1")
    testImplementation("junit:junit:4.13.2")
}

//task sourcesJar(type: Jar) {
//    from android.sourceSets.main.java.srcDirs
//    classifier = "sources"
//}
//
//task javadoc(type: Javadoc) {
//    options.charSet = "UTF-8"
//    failOnError  false
//    source = android.sourceSets.main.java.sourceFiles
//    classpath += project.files(android.getBootClasspath().join(File.pathSeparator))
//}
//
//task javadocJar(type: Jar, dependsOn: javadoc) {
//    classifier = "javadoc"
//    from javadoc.destinationDir
//}
//
//artifacts {
//    archives sourcesJar
//    archives javadocJar
//}
