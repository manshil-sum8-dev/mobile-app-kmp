import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    jacoco
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            // Security - EncryptedSharedPreferences
            implementation(libs.androidx.security.crypto)
            // Certificate pinning support
            implementation(libs.okhttp)
            implementation(libs.okhttp.tls)
            // Ktor client engine for Android
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            // Ktor client engine for iOS
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            // Networking & serialization
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            // DateTime
            implementation(libs.kotlinx.datetime)
            // Settings
            implementation(libs.multiplatform.settings)
            // DI (optional; can switch to manual providers)
            implementation(libs.koin.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }
        androidUnitTest.dependencies {
            implementation(libs.mockk)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.shot)
            implementation(libs.androidx.testExt.junit)
            implementation(libs.androidx.espresso.core)
            implementation(libs.compose.ui.testJunit4)
        }
    }
}

android {
    namespace = "za.co.quantive.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "za.co.quantive.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "SUPABASE_URL", "\"http://10.0.2.2:54321\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZS1kZW1vIiwicm9sZSI6ImFub24iLCJleHAiOjE5ODM4MTI5OTZ9.CRXP1A7WOeoJeXxjNni43kdQwgnWNReilDMblYTn_I0\"")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    testImplementation(libs.mockk)
}

// ===== COVERAGE REPORTING CONFIGURATION =====

jacoco {
    toolVersion = "0.8.8"
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)

        xml.outputLocation.set(file("${layout.buildDirectory.get().asFile}/reports/jacoco/test/jacocoTestReport.xml"))
        html.outputLocation.set(file("${layout.buildDirectory.get().asFile}/reports/jacoco/test/html"))
    }

    // Include common test source sets
    sourceDirectories.setFrom(
        files(
            "src/commonMain/kotlin",
            "src/androidMain/kotlin",
        ),
    )

    classDirectories.setFrom(
        files(
            "${layout.buildDirectory.get().asFile}/tmp/kotlin-classes/debugAndroidTest",
            "${layout.buildDirectory.get().asFile}/tmp/kotlin-classes/debug",
        ),
    )

    executionData.setFrom(
        files(
            "${layout.buildDirectory.get().asFile}/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "${layout.buildDirectory.get().asFile}/outputs/code_coverage/debugAndroidTest/connected/coverage.ec",
        ),
    )

    // Exclude generated and platform-specific code from coverage
    classDirectories.setFrom(
        classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    "**/*Test*.*",
                    "android/**/*.*",
                    "**/R.*",
                    "**/R$*.*",
                    "**/MainActivity.*",
                    "**/MainViewController.*",
                    "**/Platform.*",
                )
            }
        },
    )
}

tasks.register("jacocoCoverageVerification", JacocoCoverageVerification::class) {
    dependsOn("jacocoTestReport")

    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal() // 80% coverage requirement
            }
        }

        // Separate rules for different modules
        rule {
            element = "CLASS"
            includes = listOf("za.co.quantive.app.auth.*", "za.co.quantive.app.core.*")
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.85".toBigDecimal() // 85% for core business logic
            }
        }

        rule {
            element = "CLASS"
            includes = listOf("za.co.quantive.app.data.remote.repository.*")
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.70".toBigDecimal() // 70% for repository layer
            }
        }
    }
}

// ===== TEST EXECUTION OPTIMIZATION =====

tasks.withType<Test> {
    // Configure test execution for faster CI/CD
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1

    // Enable test result caching
    outputs.upToDateWhen { false }

    // Configure JVM arguments for better performance
    jvmArgs(
        "-Xmx2g",
        "-Xms1g",
        "-XX:MaxMetaspaceSize=512m",
        "-XX:+UseG1GC",
    )

    // Configure system properties for tests
    systemProperties(
        "junit.jupiter.execution.parallel.enabled" to "true",
        "junit.jupiter.execution.parallel.mode.default" to "concurrent",
    )
}

// ===== KOVER INTEGRATION (Alternative to Jacoco for KMP) =====

// Note: Kover is JetBrains' coverage tool with better KMP support
// Uncomment and configure if switching from Jacoco

/*
apply(plugin = "org.jetbrains.kotlinx.kover")

kover {
    filters {
        classes {
            excludes += listOf(
                "za.co.quantive.app.MainActivity",
                "za.co.quantive.app.MainViewController",
                "za.co.quantive.app.Platform*",
                "*ComposableSingletons*",
                "*_Factory*",
                "*BuildConfig*",
            )
        }
    }
}

tasks.register("koverHtmlReport") {
    dependsOn("koverGenerateHtmlReport")
    description = "Generate Kover HTML coverage report"
}

tasks.register("koverXmlReport") {
    dependsOn("koverGenerateXmlReport")
    description = "Generate Kover XML coverage report"
}

tasks.register("koverVerify") {
    dependsOn("koverVerify")
    description = "Verify coverage meets minimum thresholds"
}
*/
