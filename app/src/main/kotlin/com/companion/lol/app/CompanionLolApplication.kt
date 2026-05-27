package com.companion.lol.app

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil3.ImageLoader
import coil3.SingletonImageLoader
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class CompanionLolApplication :
  Application(), SingletonImageLoader.Factory, Configuration.Provider {
  @Inject lateinit var coilImageLoader: ImageLoader
  @Inject lateinit var workerFactory: HiltWorkerFactory

  override val workManagerConfiguration: Configuration
    get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())

      StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
          .detectAll()
          .apply {
            if (android.os.Build.VERSION.SDK_INT >= 34) {
              permitExplicitGc()
            }
          }
          .penaltyDialog()
          .penaltyLog()
          .build()
      )
      StrictMode.setVmPolicy(
        StrictMode.VmPolicy.Builder()
          .detectActivityLeaks()
          .detectLeakedClosableObjects()
          .detectLeakedRegistrationObjects()
          .detectFileUriExposure()
          .detectCleartextNetwork()
          .detectContentUriWithoutPermission()
          .build()
      )
    }
  }

  override fun newImageLoader(context: Context): ImageLoader {
    return coilImageLoader
  }
}
