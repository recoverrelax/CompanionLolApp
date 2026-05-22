package com.lol.app.ui.screens.championList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.app.BuildConfig
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.data.usecase.ChampionUseCase
import com.companion.lol.data.usecase.RefreshChampionsUseCase
import com.companion.lol.data.usecase.SettingsUseCase
import com.companion.lol.storage.impl.model.other.SortOrder
import com.lol.app.ui.screens.RefreshState
import com.lol.app.util.awaitAtLeast
import com.lol.app.util.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val showCaseArtificialAnimationDelay = if (BuildConfig.DEBUG) 2.seconds else 0.seconds

@HiltViewModel
class ChampionListViewModel
@Inject
constructor(
  private val settingsUseCase: SettingsUseCase,
  private val championUseCase: ChampionUseCase,
  private val refreshChampionsUseCase: RefreshChampionsUseCase,
) : ViewModel() {
  private val refreshState = MutableStateFlow(RefreshState())

  val state: StateFlow<ChampionListState> =
    combine(
        flow = championUseCase.observeAllWithFavorites(),
        flow2 = settingsUseCase.observe(),
        flow3 = refreshState,
        transform = { champion, settings, refreshState ->
          ChampionListState(
            champions = champion.sortBy(settings.championRotationSortOrder),
            gridSize = settings.championRotationGridSize,
            sortOrder = settings.championRotationSortOrder,
            refreshState = refreshState,
          )
        },
      )
      .stateIn(viewModelScope, SharingStarted.Eagerly, ChampionListState())

  init {
    viewModelScope.launch {
      refreshState.filter { it.refreshing }.map { it.isForced }.collectLatest(::refresh)
    }
  }

  fun onRefresh() {
    refreshState.update { it.copy(refreshing = true, isForced = true, hasError = false) }
  }

  fun onRetry() = onRefresh()

  private suspend fun refresh(isForced: Boolean) {
    if (isForced || !refreshChampionsUseCase.hasData()) {
      val success =
        awaitAtLeast(showCaseArtificialAnimationDelay) { refreshChampionsUseCase.refresh() }

      refreshState.value =
        RefreshState(
          refreshing = false,
          initialSync = false,
          isForced = isForced,
          hasError = !success,
        )
    } else {
      delay(showCaseArtificialAnimationDelay)
      refreshState.value =
        RefreshState(refreshing = false, initialSync = false, isForced = false, hasError = false)
    }
  }

  fun changeGridSize() {
    viewModelScope.launch {
      settingsUseCase.updateChampionGridSize(
        when (val value = state.value.gridSize) {
          5 -> 3
          else -> value + 1
        }
      )
    }
  }

  fun onSortMenuItemClicked() {
    viewModelScope.launch {
      settingsUseCase.updateChampionSortOrder(state.value.sortOrder.toggle())
    }
  }

  fun onFavoritesClearClicked() {
    viewModelScope.launch { championUseCase.clearFavorites() }
  }

  private fun List<ChampionModel>.sortBy(order: SortOrder): List<ChampionModel> {
    return when (order) {
      SortOrder.FAVORITES ->
        this.sortedWith(
          compareBy(
            {
              when (it.isFavorite) {
                true -> 1
                else -> 2
              }
            },
            { it.name },
          )
        )

      SortOrder.ASC -> this.sortedBy { it.name }
    }
  }
}
