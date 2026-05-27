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

  implementation(libs.dagger.hilt.android)
  ksp(libs.dagger.hilt.compiler)

  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.io.sqldelight.sqlite)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
}
