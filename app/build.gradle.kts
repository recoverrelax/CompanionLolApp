import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.stability.analyzer)
}

android {
    namespace = "com.companion.lol.app"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.companion.lol.app"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("Apk-Release"){
            keyAlias = "key0"
            keyPassword = "123321"
            storeFile = file("../keystore1")
            storePassword = "123321"
        }
    }

    buildTypes {
        release {
            applicationIdSuffix = ".R"
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("Apk-Release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

composeCompiler {
    stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("config/compose-stability-config.conf"))

    reportsDestination = rootProject.layout.projectDirectory.dir("composeReports/compose_compiler")
    metricsDestination = rootProject.layout.projectDirectory.dir("composeReports/_compiler")
}

tasks.withType<KotlinCompile> {
    compilerOptions.freeCompilerArgs.addAll(
        "-XXLanguage:+ExplicitBackingFields"
    )
}

dependencies {
    implementation(project(":data"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    //implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.io.coil)
    implementation(libs.io.coil.okhttp)
    implementation(libs.androidx.palette)

    implementation(libs.io.lottie)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.workmanager)
    implementation(libs.androidx.hilt.work)
    implementation(libs.dagger.hilt.android)
    ksp(libs.androidx.hilt.work.compiler)
    ksp(libs.dagger.hilt.compiler)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.io.timber)

    implementation(libs.kotlinx.coroutines)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}