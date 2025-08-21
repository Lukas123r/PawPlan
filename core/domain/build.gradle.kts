// core/domain/build.gradle.kts
plugins {
    kotlin("jvm")
}

// Include test libraries for JUnit4, coroutines and mocks
dependencies {
    implementation("javax.inject:javax.inject:1") // for @Inject annotations
    implementation(libs.kotlinx.coroutines.core)

    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("com.google.truth:truth:1.4.2")
}

// Compile with JDK 17 for both main and test sources
kotlin {
    jvmToolchain(17)
}

tasks.withType<Test>().configureEach {
    // Run tests with classic JUnit4 runner
    useJUnit()
}
