plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "de.lshorizon.pawplan.core.design" // Manifest package removed
    compileSdk = 36 // Build this library against Android 14 APIs

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        // Use Java 17 for this library module
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        // Align Kotlin with Java 17 bytecode
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.accompanist.systemuicontroller)
    debugImplementation(libs.androidx.ui.tooling)
}
