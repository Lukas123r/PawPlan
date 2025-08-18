// Use version catalogs to keep dependency versions centralized and readable
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    // Repositories used for resolving plugins
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
plugins {
    // Download missing JDKs automatically via Foojay
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    // Shared repositories for all modules
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PawPlan"
include(":app")
include(":core:design")
include(":core:domain")
include(":core:data")
 
