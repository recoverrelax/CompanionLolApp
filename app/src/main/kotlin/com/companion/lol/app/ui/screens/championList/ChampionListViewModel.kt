package com.companion.lol.app.ui.screens.championList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.app.BuildConfig
import com.companion.lol.app.ui.screens.RefreshState
import com.companion.lol.app.util.awaitAtLeast
import com.companion.lol.app.util.next
import com.companion.lol.app.util.sortBy
import com.companion.lol.app.util.toggle
import com.companion.lol.data.mapper.model
import com.companion.lol.data.usecase.RefreshChampionsUseCase
import com.companion.lol.data.util.listMap
import com.companion.lol.storage.impl.store.ChampionFavoritesStore
import com.companion.lol.storage.impl.store.ChampionStore
import com.companion.lol.storage.impl.store.SettingsStore
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val showcaseArtificialAnimationDelay = if (BuildConfig.DEBUG) 2.seconds else Duration.ZERO

@HiltViewModel
class ChampionListViewModel
@Inject
constructor(
  private val championStore: ChampionStore,
  private val favoritesStore: ChampionFavoritesStore,
  private val settingsStore: SettingsStore,
  private val refreshChampionsUseCase: RefreshChampionsUseCase,
) : ViewModel() {
  private val refreshState = MutableStateFlow(RefreshState())

  val state: StateFlow<ChampionListState> =
    combine(
        flow = championStore.observeAllWithFavorites().listMap(ChampionWithFavoritesView::model),
        flow2 = settingsStore.observeOrDefault(),
        flow3 = refreshState,
        transform = { champion, settings, refreshState ->
          ChampionListState(
            // few champions, otherwise flowOn down the line
            champions = champion.sortBy(settings.championSortOrder),
            gridSize = settings.championGridSize,
            sortOrder = settings.championSortOrder,
            refreshState = refreshState,
          )
        },
      )
      .stateIn(viewModelScope, SharingStarted.Eagerly, ChampionListState())

  init {
    viewModelScope.launch {
      refreshState.filter { it.refreshing }.map { it.userTriggered }.collectLatest(::refresh)
    }
  }

  fun onRefresh() {
    refreshState.value = RefreshState(refreshing = true, userTriggered = true, hasError = false)
  }

  fun onRetry() = onRefresh()

  private suspend fun refresh(userTriggered: Boolean) {
    if (userTriggered || !championStore.hasData()) {
      val success =
        awaitAtLeast(showcaseArtificialAnimationDelay) { refreshChampionsUseCase.refresh() }
          .isSuccess

      refreshState.value =
        RefreshState(refreshing = false, userTriggered = userTriggered, hasError = !success)
    } else {
      delay(showcaseArtificialAnimationDelay)
      refreshState.value = RefreshState(refreshing = false, userTriggered = false, hasError = false)
    }
  }

  fun changeGridSize() {
    viewModelScope.launch { settingsStore.insert(championGridSize = state.value.gridSize.next()) }
  }

  fun onSortMenuItemClicked() {
    viewModelScope.launch {
      settingsStore.insert(championSortOrder = state.value.sortOrder.toggle())
    }
  }

  fun onFavoritesClearClicked() {
    viewModelScope.launch { favoritesStore.clearAll() }
  }
}
