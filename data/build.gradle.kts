plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.companion.lol.data"
  compileSdk { version = release(36) }
}

dependencies {
  implementation(project(":network"))
  api(project(":storage:impl"))

  implementation(libs.io.timber)
  implementation(libs.android.hilt)
  ksp(libs.android.hilt.compiler)

  compileOnly(libs.androidx.compose.runtime.annotation)
}
