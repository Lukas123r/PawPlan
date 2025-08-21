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
  compileSdk = 36
  defaultConfig {
    minSdk = 24
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions { jvmTarget = "17" }
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

  // Library module for data layer
}
