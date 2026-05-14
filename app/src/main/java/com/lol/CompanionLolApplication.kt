package com.lol

import android.app.Application
import com.companion.lol.app.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CompanionLolApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      (Timber.plant(Timber.DebugTree()))
    }
  }
}
