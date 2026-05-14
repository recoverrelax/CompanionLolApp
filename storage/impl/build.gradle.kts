plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.companion.lol.impl"
  compileSdk { version = release(36) }
}

dependencies {
  api(project(":storage:sqldelight"))
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core.ktx)
  implementation(libs.material)

  implementation(libs.android.hilt)
  ksp(libs.android.hilt.compiler)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
}
