package com.companion.lol.impl.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.companion.lol.impl.util.DbTransactor
import com.companion.lol.sqldelight.LolAppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object StorageModule {

  @Provides
  @Singleton
  fun driver(@ApplicationContext context: Context): SqlDriver =
    AndroidSqliteDriver(LolAppDb.Schema, context, "com.lol.storage.db")

  @Provides
  @Singleton
  fun transacter(dataBase: LolAppDb): DbTransactor =
    object : DbTransactor {
      override fun runInTransaction(body: () -> Unit) {
        dataBase.transaction { body() }
      }
    }

  @Provides @Singleton fun database(driver: SqlDriver): LolAppDb = LolAppDb(driver = driver)
}
