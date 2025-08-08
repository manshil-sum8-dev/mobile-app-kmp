plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false

    // Quality gates
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false

    // Feature scaffolding plugin
    id("FeatureGeneratorPlugin")
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    // Configure ktlint for each project
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("0.50.0")
        debug.set(false)
        verbose.set(false)
        android.set(true)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(false)

        filter {
            exclude("**/build/**")
            exclude("**/buildSrc/**")
            exclude("**/generated/**")
            exclude { it.file.path.contains("build/") }
        }
    }

    // Configure detekt for each project
    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        baseline = file("$rootDir/config/detekt/baseline.xml")

        reports {
            html.required.set(true)
            xml.required.set(true)
            txt.required.set(false)
            sarif.required.set(false)
        }
    }
}

// Quality check tasks
tasks.register("qualityCheck") {
    group = "quality"
    description = "Run all quality checks"
    dependsOn("ktlintCheck", "detekt")
}

tasks.register("qualityFix") {
    group = "quality"
    description = "Fix all auto-fixable quality issues"
    dependsOn("ktlintFormat")
}
