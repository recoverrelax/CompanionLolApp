// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp)  apply false
    alias(libs.plugins.detekt) apply true
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("${layout.buildDirectory.get().asFile}/**/*.kt")

            ktfmt().googleStyle()
        }
    }

    // Configure detekt for each subproject
    detekt {
        config.setFrom(rootProject.file("config/detekt/detekt.yml"))
        parallel = true
        autoCorrect = true
        buildUponDefaultConfig = true
        basePath = rootProject.projectDir.path
    }
}

