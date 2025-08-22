// core/data/build.gradle.kts
// Build script for the data layer with Hilt bindings
plugins {
  id("com.android.library")
  kotlin("android")
  alias(libs.plugins.ksp) // KSP handles annotation processing
  id("com.google.dagger.hilt.android") // enables Hilt code generation
}

android {
  namespace = "de.lshorizon.pawplan.core.data"
  compileSdk = 33 // Build against Android 13 (API 33)
  defaultConfig {
    minSdk = 24
    targetSdk = 33 // Target Android 13 for runtime behaviour
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions { jvmTarget = "17" }

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
  implementation(projects.core.domain)
  implementation(libs.kotlinx.coroutines.core)

  // Preferences DataStore for simple key-value persistence
  implementation(libs.androidx.datastore.preferences)
  // Core DataStore library for type-safe storage
  implementation(libs.androidx.datastore.core)

  // Dependency injection with Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.compiler)

  // Test dependencies for reliable JVM tests
  testImplementation("junit:junit:4.13.2") // Standard JUnit4 assertions
  testImplementation("org.robolectric:robolectric:4.10.3") // JVM tests with Android resources
}
