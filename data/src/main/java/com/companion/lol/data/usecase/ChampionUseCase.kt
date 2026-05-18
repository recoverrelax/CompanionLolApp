package com.companion.lol.data.usecase

import com.companion.lol.data.mapper.model
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.store.ChampionFavoritesStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.util.withDbContext
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView
import com.companion.lol.util.listMap
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionUseCase @Inject constructor(
    private val championStore: ChampionStore,
    private val favoritesStore: ChampionFavoritesStore
) {
    fun observeAllWithFavorites(): Flow<List<ChampionModel>>{
        return championStore.observeAllWithFavorites()
            .listMap(ChampionWithFavoritesView::model)
    }

    suspend fun markFavourite(championId: ChampionId, isFavourite: Boolean) = withDbContext{
        favoritesStore.markFavorite(championId = championId, isFavorite = isFavourite)
    }
}