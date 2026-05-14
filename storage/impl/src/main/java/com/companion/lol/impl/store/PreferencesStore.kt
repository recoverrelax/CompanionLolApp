package com.companion.lol.impl.store

import com.companion.lol.impl.util.withDbContext
import com.companion.lol.sqldelight.LolAppDb
import com.lol.storage.tables.PreferencesEntity
import com.lol.storage.tables.PreferencesQueries
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_ID = 0L

@Singleton
class PreferencesStore @Inject constructor(database: LolAppDb) {
  private val preferencesQueries: PreferencesQueries = database.preferencesQueries

  suspend fun get(): PreferencesEntity? = preferencesQueries.get().executeAsOneOrNull()

  suspend fun insert(details: PreferencesEntity) = withDbContext {
    preferencesQueries.insert(details.copy(id = USER_ID))
  }

  suspend fun delete() = withDbContext { preferencesQueries.delete().await() }
}
