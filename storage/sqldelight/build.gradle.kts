plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.io.sqldelight)
}

android {
  namespace = "com.companion.lol.sqldelight"
  compileSdk { version = release(36) }
}

sqldelight {
  databases {
    create("LolAppDb"){
      packageName.set("com.companion.lol.storage.sqldelight")

    }
  }
}

dependencies {
  api(libs.io.sqldelight.android)
  api(libs.io.sqldelight.adapters)
  api(libs.io.sqldelight.coroutines)
  api(libs.kotlinx.datetime)

  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.io.sqldelight.sqlite)

  compileOnly(libs.androidx.compose.runtime.annotation)
}
