package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.impl.util.RequiresDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsQueries
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class ChampionDetailsStore
@Inject
constructor(database: LolAppDb, private val dbDispatcher: DbDispatcher) :
  SqldelightStore<ChampionDetailsQueries>(database.championDetailsQueries) {

  @RequiresDispatcher fun insertSync(details: ChampionDetailsTable) = queries.insert(details)

  fun observeByID(championId: ChampionId): Flow<ChampionDetailsTable?> =
    queries.findById(championId).asFlow().mapToOneOrNull(dbDispatcher)
}
