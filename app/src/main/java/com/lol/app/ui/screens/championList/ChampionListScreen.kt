package com.lol.app.ui.screens.championList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.lol.app.util.ChampionColorCache

@Composable
fun ChampionListScreen() {
  val viewModel = hiltViewModel<ChampionListViewModel>()
  val state by viewModel.state.collectAsState()
  val championColorCache = viewModel.championColorCache

  ChampionListScreen(
    state = state,
    championColorCache = championColorCache,
    onCardClick = viewModel::onCardClick,
    onGridSizeItemMenuClicked = viewModel::changeGridSize,
    onFavoritesToggled = viewModel::onFavoritesToggled,
  )
}

@Composable
fun ChampionListScreen(
  state: ChampionListState,
  championColorCache: ChampionColorCache,
  onCardClick: (ChampionId) -> Unit,
  onGridSizeItemMenuClicked: () -> Unit,
  onFavoritesToggled: (ChampionId) -> Unit,
) {

  Scaffold(
    topBar = {
      ChampionListToolbar(
        modifier = Modifier.fillMaxWidth(),
        sortOrder = state.sortOrder,
        onGridSizeItemMenuClicked = onGridSizeItemMenuClicked,
        onOrderItemMenuClicked = {},
        onFavoritesItemMenuClicked = {},
      )
    }
  ) { contentPadding ->
    LazyVerticalGrid(
      modifier = Modifier.fillMaxSize(),
      columns = GridCells.Fixed(state.gridSize),
      contentPadding =
        PaddingValues(
          top = 16.dp + contentPadding.calculateTopPadding(),
          bottom = 16.dp,
          start = 16.dp,
          end = 16.dp,
        ),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items(items = state.champions, key = { it.id.value }) { item ->
        ChampionCard(
          modifier = Modifier.fillMaxWidth(),
          champion = item,
          championColorCache = championColorCache,
          onCardClick = onCardClick,
          gridSize = state.gridSize,
        )
      }
    }
  }
}
