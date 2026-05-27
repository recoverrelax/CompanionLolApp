package com.companion.lol.data.usecase

import com.companion.lol.data.other.CompletableResult
import com.companion.lol.data.util.getOrPropagate
import com.companion.lol.network.DDragonApi
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.ids.SkinId
import com.companion.lol.storage.impl.model.other.ChampionTag
import com.companion.lol.storage.impl.model.other.PartyType
import com.companion.lol.storage.impl.store.ChampionDetailsStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SkinStore
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.impl.util.DbTransacter
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsTable
import com.companion.lol.storage.sqldelight.tables.SkinTable
import javax.inject.Inject
import kotlinx.coroutines.withContext
import timber.log.Timber

class RefreshChampionDetailsUseCase
@Inject
constructor(
  private val championStore: ChampionStore,
  private val championDetailsStore: ChampionDetailsStore,
  private val skinsStore: SkinStore,
  private val dDragonApi: DDragonApi,
  private val transacter: DbTransacter,
  private val dbDispatcher: DbDispatcher,
) {
  suspend fun refresh(championId: ChampionId): CompletableResult =
    withContext(dbDispatcher) {
      val championKeyName = championStore.findKeyNameById(championId)

      val champion =
        dDragonApi
          .getChampionDetails(championName = championKeyName)
          .getOrPropagate {
            Timber.e(it)
            return@withContext Result.failure(it)
          }
          .info

      val skins =
        champion.skins
          // parentSkin are chroma skins, don't map directly to url
          .filter { it.parentSkin == null }
          .map {
            SkinTable(
              id = championId,
              skinId = SkinId(it.id),
              number = it.num,
              name = it.name,
              isChroma = it.chromas,
            )
          }

      transacter.transaction {
        championDetailsStore.insertSync(
          ChampionDetailsTable(
            id = championId,
            lore = champion.lore,
            blurb = champion.blurb,
            tags = champion.tags.map { ChampionTag.from(it) },
            partyTypeId = PartyType.from(champion.partyType).dbId,
          )
        )
        skinsStore.insertAllSync(skins)
      }
      return@withContext Result.success(Unit)
    }
}
