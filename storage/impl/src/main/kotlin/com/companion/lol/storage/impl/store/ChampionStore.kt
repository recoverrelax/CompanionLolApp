package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.impl.util.RequiresDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.ChampionQueries
import com.companion.lol.storage.sqldelight.tables.ChampionTable
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Singleton
class ChampionStore
@Inject
constructor(database: LolAppDb, private val dbDispatcher: DbDispatcher) :
  SqldelightStore<ChampionQueries>(database.championQueries) {
  @RequiresDispatcher
  fun insertAllSync(champions: List<ChampionTable>) {
    queries.transaction { champions.forEach { queries.insert(it) } }
  }

  suspend fun hasData(): Boolean = withContext(dbDispatcher) { queries.hasData().executeAsOne() }

  fun observeAllWithFavorites(): Flow<List<ChampionWithFavoritesView>> =
    queries.findAll().asFlow().mapToList(dbDispatcher)

  fun observeWithFavoritesById(championId: ChampionId): Flow<ChampionWithFavoritesView> =
    queries.findById(championId).asFlow().mapToOne(dbDispatcher)

  suspend fun findKeyNameById(championId: ChampionId): String =
    withContext(dbDispatcher) { queries.findKeyNameById(championId).executeAsOne() }
}
