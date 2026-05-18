package com.lol.app.ui.screens.championList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.ChampionUseCase
import com.companion.lol.data.usecase.SettingsUseCase
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.lol.app.util.ChampionColorCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ChampionListViewModel
@Inject
constructor(
  private val championStore: ChampionUseCase,
  private val settingsStore: SettingsUseCase,
) : ViewModel() {

  val championColorCache: ChampionColorCache
    = ChampionColorCache.Impl(viewModelScope)
  val state: StateFlow<ChampionListState> =
    combine(
        flow = championStore.observeAllWithFavorites(),
        flow2 = settingsStore.observe(),
        transform = { champion, settings ->
          ChampionListState(
            champions = champion,
            gridSize = settings.championRotationGridSize,
            sortOrder = settings.championRotationSortOrder,
          )
        },
      )
      .stateIn(viewModelScope, SharingStarted.Eagerly, ChampionListState())

  fun onCardClick(championId: ChampionId) {}

  fun changeGridSize() {
    viewModelScope.launch {
      settingsStore.updateChampionRotationGridSize(
        when (val value = state.value.gridSize) {
          5 -> 3
          else -> value + 1
        }
      )
    }
  }

  fun onFavoritesToggled(championId: ChampionId) {
    val champion = state.value.champions.first { it.id == championId }

    viewModelScope.launch {
      championStore.markFavourite(championId = champion.id, isFavourite = champion.isFavorite.not())
    }
  }
}
