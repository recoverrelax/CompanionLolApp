@file:OptIn(ExperimentalCoroutinesApi::class)

package com.companion.lol.data.usecase

import com.companion.lol.data.mapper.model
import com.companion.lol.data.model.ChampionDetailsModel
import com.companion.lol.data.model.ChampionWithDetailsModel
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.ChampionDetailsStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SkinStore
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@Singleton
class ChampionWithDetailsUseCase
@Inject
constructor(
  private val championStore: ChampionStore,
  private val skinsStore: SkinStore,
  private val championDetailsStore: ChampionDetailsStore,
) {
  fun observeChampionWithDetails(championId: ChampionId): Flow<ChampionWithDetailsModel> =
    flow { emit(championStore.findKeyNameById(championId)) }
      .flatMapLatest { keyName ->
        combine(
          championStore.observeWithFavoritesById(championId).map { it.model() },
          observeChampionDetails(championId, keyName),
          ::ChampionWithDetailsModel,
        )
      }

  private fun observeChampionDetails(
    championId: ChampionId,
    keyName: String,
  ): Flow<ChampionDetailsModel?> =
    combine(
      championDetailsStore.observeByID(championId),
      skinsStore.observeByChampionId(championId),
    ) { details, skins ->
      details?.model(keyName, skins)
    }
}
