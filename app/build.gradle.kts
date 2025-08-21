plugins {
    // Application and Kotlin plugins with Compose, Hilt and KSP support
    id("com.android.application")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.kapt) // enable kapt for Hilt testing
    id("androidx.room") version "2.6.1" // enables Room specific Gradle extensions
    id("app.cash.paparazzi") version "1.3.2" // snapshot testing without an emulator
}

android {
    // Module namespace used for resources and R class generation
    namespace = "de.lshorizon.pawplan"
    compileSdk = 36 // Build against Android 15 (API 36) for newer AndroidX

    defaultConfig {
        applicationId = "de.lshorizon.pawplan" // unique app identifier
        minSdk = 24
        targetSdk = 36 // Target Android 15 APIs for compatibility
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
        // Target Java 17 for modern language features
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // Enable desugaring for java.time on older devices
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        // Generate Kotlin bytecode for Java 17
        jvmTarget = "17"
    }
    buildFeatures {
        // Enable Jetpack Compose
        compose = true
    }
    testOptions {
        // Disable animations for stable UI tests
        animationsDisabled = true
        // Make Android resources visible to JVM tests (Robolectric & Paparazzi)
        unitTests.isIncludeAndroidResources = true
        unitTests.all {
            // Tweak JVM for stable, verbose testing on CI
            it.jvmArgs(
                "-XX:+UseParallelGC",
                "-Xmx2g",
                "-Dfile.encoding=UTF-8",
            )
            it.testLogging {
                events("failed", "skipped", "standardError")
                showCauses = true
                showExceptions = true
                showStackTraces = true
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }
    }
    packaging {
        // Exclude license files required by Compose testing
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Desugaring library for java.time backport
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(projects.core.design)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    // Local data persistence and Room setup
    implementation(libs.androidx.datastore.preferences)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    // Dependency injection with Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.hilt:hilt-work:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    // Background work handling
    implementation("androidx.work:work-runtime-ktx:2.9.1")
    // Extended icon set for pets etc.
    implementation("androidx.compose.material:material-icons-extended")
    // Compose lifecycle helpers
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.2")
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    // Robolectric for tests that reference Android SDK APIs
    testImplementation("org.robolectric:robolectric:4.10.3")
    // Hilt testing support
    val hiltVersion = if (rootProject.extra.has("hiltVersion")) rootProject.extra["hiltVersion"] as String else "2.51.1"
    testImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
    // Assertions with Truth
    testImplementation("com.google.truth:truth:1.4.4")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.work.testing)
    // Core instrumentation test libraries
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(platform("org.jetbrains.kotlin:kotlin-bom")) // Align Kotlin versions in tests
    testImplementation("junit:junit:4.13.2") // Standard JUnit assertions
    testImplementation("androidx.compose.ui:ui-test-junit4") // Compose testing utilities for Paparazzi
}

// Configure Room to export schemas for version tracking
room {
    schemaDirectory("$projectDir/schemas")
}

// Pass annotation processor arguments to Room via KSP
ksp {
    arg("room.incremental", "true")
}
