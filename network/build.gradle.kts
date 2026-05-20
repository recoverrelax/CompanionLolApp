import java.util.Properties

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.companion.lol.network"
  compileSdk { version = release(36) }

  buildFeatures {
    buildConfig = true
  }

  defaultConfig {
    // Load the property
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
      localProperties.load(localPropertiesFile.inputStream())
    }

    val apiKey = localProperties.getProperty("riotApiKey") ?: "\"\""

    // Create the BuildConfig field
    buildConfigField("String", "RIOT_API_KEY", apiKey)
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlinx.serialization.InternalSerializationApi")
    }
}

dependencies {
  implementation(libs.androidx.appcompat)

  implementation(libs.kotlinx.serialization)

  implementation(libs.android.hilt)
  ksp(libs.android.hilt.compiler)

  implementation(libs.io.timber)

  implementation(libs.retrofit.retrofit2)
  implementation(libs.retrofit.logging)
  implementation(libs.retrofit.serializationConverter)
}
