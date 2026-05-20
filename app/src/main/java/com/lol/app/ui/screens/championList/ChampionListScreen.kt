@file:OptIn(ExperimentalMaterial3Api::class)

package com.lol.app.ui.screens.championList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.companion.lol.storage.impl.model.ids.ChampionId

@Composable
fun ChampionListScreen(onCardClick: (ChampionId) -> Unit) {
  val viewModel = hiltViewModel<ChampionListViewModel>()
  val state by viewModel.state.collectAsState()

  ChampionListScreen(
    state = state,
    onCardClick = onCardClick,
    onGridSizeItemMenuClicked = viewModel::changeGridSize,
    onSortMenuItemClicked = viewModel::onSortMenuItemClicked,
    onFavoritesClearClicked = viewModel::onFavoritesClearClicked,
  )
}

@Composable
fun ChampionListScreen(
  state: ChampionListState,
  onCardClick: (ChampionId) -> Unit,
  onGridSizeItemMenuClicked: () -> Unit,
  onSortMenuItemClicked: () -> Unit,
  onFavoritesClearClicked: () -> Unit,
) {

  val listState = rememberLazyGridState()

  LaunchedEffect(state.sortOrder) { listState.scrollToItem(0) }

  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      ChampionListToolbar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = scrollBehavior,
        sortOrder = state.sortOrder,
        onGridSizeItemMenuClicked = onGridSizeItemMenuClicked,
        onSortMenuItemClicked = onSortMenuItemClicked,
        onClearFavoritesMenuItemClicked = onFavoritesClearClicked,
      )
    },
    containerColor = MaterialTheme.colorScheme.surface,
  ) { contentPadding ->
    LazyVerticalGrid(
      modifier = Modifier.fillMaxSize(),
      state = listState,
      columns = GridCells.Fixed(state.gridSize),
      contentPadding =
        PaddingValues(
          top = 4.dp + contentPadding.calculateTopPadding(),
          bottom = 4.dp,
          start = 4.dp,
          end = 4.dp,
        ),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      items(items = state.champions, key = { it.id.value }) { item ->
        ChampionCard(
          modifier = Modifier.fillMaxWidth().animateItem(),
          champion = item,
          onCardClick = onCardClick,
          gridSize = state.gridSize,
        )
      }
    }
  }
}
