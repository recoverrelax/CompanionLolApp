package com.companion.lol.data.usecase

import com.companion.lol.network.DDragonApi
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.other.PartyType
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.PartyTypeStore
import com.companion.lol.storage.impl.util.CompanionLolTransactor
import com.companion.lol.storage.impl.util.dbDispatcher
import com.companion.lol.storage.impl.util.withDbContext
import com.companion.lol.storage.sqldelight.tables.ChampionPartyTypeTable
import com.companion.lol.storage.sqldelight.tables.ChampionTable
import com.companion.lol.util.capitalizeWords
import com.companion.lol.util.catchIoException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.invoke

@Singleton
class RefreshChampionsUseCase
@Inject
constructor(
  private val championStore: ChampionStore,
  private val partyTypeStore: PartyTypeStore,
  private val api: DDragonApi,
  private val transacter: CompanionLolTransactor,
) {
  suspend fun refresh(): Boolean = withDbContext {
    val champions = catchIoException { api.getChampionList() } ?: return@withDbContext false

    transacter.transaction {
      champions.champions
        .map {
          ChampionTable(
            id = ChampionId(it.championKey),
            keyName = it.id,
            name = it.name,
            title = it.title.capitalizeWords(),
            squareImageName = it.image.full,
            partTypeId = PartyType.from(it.partType).dbId,
          )
        }
        .also {
          championStore.insertAll(it)
          partyTypeStore.insertAll(
            it
              .distinctBy { item -> item.partTypeId }
              .map { item -> PartyType.from(item.partTypeId) }
              .map { partyType -> ChampionPartyTypeTable(id = partyType.dbId, partyType) }
          )
        }
    }
    return@withDbContext true
  }

  suspend fun hasData(): Boolean = dbDispatcher {
    return@dbDispatcher championStore.hasData()
  }
}
