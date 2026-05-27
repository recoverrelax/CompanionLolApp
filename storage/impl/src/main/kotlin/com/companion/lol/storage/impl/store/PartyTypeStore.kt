package com.companion.lol.storage.impl.store

import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.RequiresDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.ChampionPartyTypeQueries
import com.companion.lol.storage.sqldelight.tables.ChampionPartyTypeTable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PartyTypeStore @Inject constructor(database: LolAppDb) :
  SqldelightStore<ChampionPartyTypeQueries>(database.championPartyTypeQueries) {
  @RequiresDispatcher
  fun insertAllSync(partyTypes: List<ChampionPartyTypeTable>) {
    queries.transaction { partyTypes.forEach(queries::insert) }
  }
}
