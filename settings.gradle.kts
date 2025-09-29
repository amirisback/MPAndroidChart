pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}


include("MPChartLib")
include("MPChartExample")
//include 'MPAndroidChart-Realm'
//include ':MPChartLib-Realm'
//project(':MPChartLib-Realm').projectDir = new File('../MPAndroidChart-Realm/MPChartLib-Realm')


