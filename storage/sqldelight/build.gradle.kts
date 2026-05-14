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
    create("LolAppDb")
  }
}

dependencies {
  api(libs.io.sqldelight)
  api(libs.io.sqldelight.adapters)
  api(libs.io.sqldelight.flow)
}
