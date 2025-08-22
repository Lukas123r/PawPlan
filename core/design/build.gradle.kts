plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "de.lshorizon.pawplan.core.design" // Manifest package removed
    compileSdk = 33 // Build this library against Android 13 APIs

    defaultConfig {
        minSdk = 24
        targetSdk = 33 // Target Android 13 for consistent behaviour
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
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all {
            it.testLogging {
                events("failed", "skipped", "standardError")
                showCauses = true
                showExceptions = true
                showStackTraces = true
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }
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
    testImplementation("junit:junit:4.13.2") // Standard JUnit4 assertions
    testImplementation("org.robolectric:robolectric:4.10.3") // JVM tests with Android resources
    testImplementation("androidx.compose.ui:ui-test-junit4") // Compose UI assertions
}
