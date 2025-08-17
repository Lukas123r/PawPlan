// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Base Android and Kotlin support for subprojects
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    // Hilt and KSP plugins are declared here for reuse in modules
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}
