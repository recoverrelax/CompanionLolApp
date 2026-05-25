package com.companion.lol.data.usecase

import com.companion.lol.data.other.CompletableResult
import com.companion.lol.data.util.capitalizeWords
import com.companion.lol.data.util.getOrPropagate
import com.companion.lol.network.DDragonApi
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.other.PartyType
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.PartyTypeStore
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.impl.util.DbTransacter
import com.companion.lol.storage.sqldelight.tables.ChampionPartyTypeTable
import com.companion.lol.storage.sqldelight.tables.ChampionTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.withContext
import timber.log.Timber

@Singleton
class RefreshChampionsUseCase
@Inject
constructor(
  private val championStore: ChampionStore,
  private val partyTypeStore: PartyTypeStore,
  private val api: DDragonApi,
  private val transacter: DbTransacter,
  private val dbDispatcher: DbDispatcher,
) {
  suspend fun refresh(): CompletableResult =
    withContext(dbDispatcher) {
      val champions =
        api.getChampionList().getOrPropagate {
          Timber.e(it)
          return@withContext Result.failure(it)
        }

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
            championStore.insertAllSync(it)
            partyTypeStore.insertAllSync(
              it
                .distinctBy { item -> item.partTypeId }
                .map { item -> PartyType.from(item.partTypeId) }
                .map { partyType -> ChampionPartyTypeTable(id = partyType.dbId, partyType) }
            )
          }
      }
      return@withContext Result.success(Unit)
    }
}
