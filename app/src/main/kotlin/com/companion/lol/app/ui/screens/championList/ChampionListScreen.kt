@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.app.ui.screens.championList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.companion.lol.app.util.count
import com.companion.lol.storage.impl.model.ids.ChampionId

@Composable
fun ChampionListScreen(onCardClick: (ChampionId) -> Unit) {
  val viewModel = hiltViewModel<ChampionListViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()

  ChampionListScreen(
    state = state,
    onCardClick = onCardClick,
    onGridSizeItemMenuClicked = viewModel::changeGridSize,
    onSortMenuItemClicked = viewModel::onSortMenuItemClicked,
    onFavoritesClearClicked = viewModel::onFavoritesClearClicked,
    onRefresh = viewModel::onRefresh,
    onRetry = viewModel::onRetry,
  )
}

@Composable
fun ChampionListScreen(
  state: ChampionListState,
  onCardClick: (ChampionId) -> Unit,
  onGridSizeItemMenuClicked: () -> Unit,
  onSortMenuItemClicked: () -> Unit,
  onFavoritesClearClicked: () -> Unit,
  onRefresh: () -> Unit,
  onRetry: () -> Unit,
) {

  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      ChampionListToolbar(
        modifier = Modifier.fillMaxWidth(),
        // needed to prevent scroll ON the AppBar while refreshing
        scrollBehavior = if (state.isRefreshing) null else scrollBehavior,
        sortOrder = state.sortOrder,
        onGridSizeItemMenuClicked = onGridSizeItemMenuClicked,
        onSortMenuItemClicked = onSortMenuItemClicked,
        onClearFavoritesMenuItemClicked = onFavoritesClearClicked,
      )
    },
  ) { contentPadding ->
    val topPadding = contentPadding.calculateTopPadding()

    ChampionListPullRefreshBox(
      modifier = Modifier.fillMaxWidth(),
      isRefreshing = state.isRefreshing,
      onRefresh = onRefresh,
      indicatorContentPadding = contentPadding,
    ) {
      if (!state.showError) {
        LazyVerticalGrid(
          modifier = Modifier.fillMaxSize(),
          // prevent scrolling ON the list while refreshing
          userScrollEnabled = !state.isRefreshing,
          columns = GridCells.Fixed(state.gridSize.count),
          contentPadding =
            PaddingValues(start = 4.dp, end = 4.dp, top = 4.dp + topPadding, bottom = 4.dp),
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
      } else {
        SyncErrorContent(modifier = Modifier.fillMaxSize(), onRetry = onRetry)
      }
    }
  }
}
