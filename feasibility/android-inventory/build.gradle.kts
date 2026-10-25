plugins {
    id("com.android.application") version "9.2.0"
}

android {
    namespace = "livestream.feasibility.inventory"
    compileSdk = 37

    defaultConfig {
        applicationId = "livestream.feasibility.inventory"
        minSdk = 31
        targetSdk = 37
        versionCode = 1
        versionName = "m2-inventory-1"
    }

    signingConfigs.getByName("debug") {
        storeFile = rootProject.file("../../.tools/m2/inventory-debug.keystore")
    }
}
