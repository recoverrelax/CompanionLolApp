package com.lol.app.di

import android.app.Application
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.lol.app.io.initializer.Initializers
import com.lol.app.io.initializer.InitializersImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

  @Singleton
  @Provides
  internal fun initializers(impl: InitializersImpl): Initializers = impl

  @Provides
  @Singleton
  internal fun coilImageLoader(okhttpClient: OkHttpClient, context: Application): ImageLoader =
    ImageLoader.Builder(context)
      .components { add(OkHttpNetworkFetcherFactory(callFactory = { okhttpClient })) }
      .logger(DebugLogger())
      .crossfade(true)
      .build()
}
