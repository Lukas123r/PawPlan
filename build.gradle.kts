// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension

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

subprojects {
    // Align all Android modules with API 36 to satisfy newer dependencies
    plugins.withId("com.android.application") {
        extensions.configure(ApplicationExtension::class) {
            compileSdk = 36 // Build against Android 14 (API 36)
            defaultConfig { targetSdk = 36 } // Target the latest Android
        }
    }
    plugins.withId("com.android.library") {
        extensions.configure(LibraryExtension::class) {
            compileSdk = 36 // Keep library modules on Android 14 APIs
        }
    }

    // Ensure Java-based modules use JDK 17
    plugins.withType<JavaPlugin> {
        extensions.configure(JavaPluginExtension::class) {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

    // Configure Kotlin JVM modules to use JDK 17
    plugins.withId("org.jetbrains.kotlin.jvm") {
        extensions.configure(org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension::class) {
            jvmToolchain(17)
        }
    }
    // Configure Kotlin Android modules to use JDK 17
    plugins.withId("org.jetbrains.kotlin.android") {
        extensions.configure(org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension::class) {
            jvmToolchain(17)
        }
    }

    // Fallback: make all Kotlin compile tasks target Java 17
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "17"
    }
}

