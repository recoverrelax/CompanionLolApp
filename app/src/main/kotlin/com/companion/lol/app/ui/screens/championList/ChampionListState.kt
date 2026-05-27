package com.companion.lol.app.ui.screens.championList

import androidx.compose.runtime.Stable
import com.companion.lol.app.base.ComposeState
import com.companion.lol.app.ui.screens.RefreshState
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.storage.impl.model.other.GridSize
import com.companion.lol.storage.impl.model.other.SortOrder

@Stable
data class ChampionListState(
  val champions: List<ChampionModel> = emptyList(),
  val gridSize: GridSize = GridSize.MEDIUM,
  val sortOrder: SortOrder = SortOrder.ASC,
  private val refreshState: RefreshState = RefreshState(),
) : ComposeState {
  val isRefreshing: Boolean = refreshState.refreshing

  val showError: Boolean = refreshState.hasError && champions.isEmpty()
}
