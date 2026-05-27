package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import com.companion.lol.storage.impl.model.ids.SettingsId
import com.companion.lol.storage.impl.model.other.GridSize
import com.companion.lol.storage.impl.model.other.SortOrder
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.SettingsQueries
import com.companion.lol.storage.sqldelight.tables.SettingsTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

private val default = SettingsTable(SettingsId, GridSize.MEDIUM, SortOrder.ASC)

@Singleton
class SettingsStore
@Inject
constructor(database: LolAppDb, private val dbDispatcher: DbDispatcher) :
  SqldelightStore<SettingsQueries>(database.settingsQueries) {
  private fun findOrDefaultSync(): SettingsTable = queries.findAll().executeAsOneOrNull() ?: default

  fun observeOrDefault(): Flow<SettingsTable> =
    queries.findAll().asFlow().mapToOneOrDefault(default, dbDispatcher)

  suspend fun insert(championGridSize: GridSize? = null, championSortOrder: SortOrder? = null) =
    withContext(dbDispatcher) {
      queries.transaction {
        val current = findOrDefaultSync()
        queries.insert(
          SettingsTable(
            id = SettingsId,
            championGridSize = championGridSize ?: current.championGridSize,
            championSortOrder = championSortOrder ?: current.championSortOrder,
          )
        )
      }
    }
}
