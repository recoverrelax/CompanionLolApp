package com.companion.lol.network.di

import com.companion.lol.network.BuildConfig
import com.companion.lol.network.DDragonApi
import com.companion.lol.network.EndPoints
import com.companion.lol.network.interceptors.RiotDevPortalApiKeyInterceptor
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

  @OptIn(ExperimentalSerializationApi::class)
  @Provides
  @Singleton
  internal fun baseRetrofit(okhttpClient: OkHttpClient): Retrofit {
    // This is an ugly hack
    // But StrictMode identifies violation here
    // somewhere in the initialization. We could theoretically
    // get this dependency as Lazy<*>, but that would mean do it everywhere
    @Suppress("JSON_FORMAT_REDUNDANT")
    return runBlocking(Dispatchers.IO) {
      Retrofit.Builder()
        .baseUrl(EndPoints.DDragon.BASE) // we replace this later
        .addConverterFactory(
          Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType())
        )
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(okhttpClient)
        .build()
    }
  }

  @Provides
  @Singleton
  internal fun httpInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
  }

  @Provides
  @Singleton
  internal fun baseOkhttpClient(
    logInterceptor: HttpLoggingInterceptor,
    apiKeyInterceptor: RiotDevPortalApiKeyInterceptor,
  ): OkHttpClient =
    OkHttpClient.Builder()
      .apply {
        if (BuildConfig.DEBUG) {
          addInterceptor(logInterceptor)
        }
        addInterceptor(apiKeyInterceptor)
      }
      .build()

  @Provides
  @Singleton
  internal fun dDragonApi(retrofit: Retrofit): DDragonApi {
    return retrofit.create(DDragonApi::class.java)
  }
}
