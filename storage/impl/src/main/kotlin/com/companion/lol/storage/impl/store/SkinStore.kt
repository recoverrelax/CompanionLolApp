package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.impl.util.RequiresDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.SkinTable
import com.companion.lol.storage.sqldelight.tables.SkinTableQueries
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class SkinStore
@Inject
constructor(private val database: LolAppDb, private val dbDispatcher: DbDispatcher) :
  SqldelightStore<SkinTableQueries>(database.skinTableQueries) {

  fun observeByChampionId(championId: ChampionId): Flow<List<SkinTable>> {
    return queries.findByChampionId(championId).asFlow().mapToList(dbDispatcher)
  }

  @RequiresDispatcher
  fun insertAllSync(skins: List<SkinTable>) =
    database.transaction { skins.forEach(queries::insert) }
}
