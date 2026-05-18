package com.lol.app.ui.screens.championList

import androidx.compose.runtime.Stable
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.storage.impl.model.other.SortOrder
import com.lol.app.base.ComposeState

@Stable
data class ChampionListState(
    val champions: List<ChampionModel> = emptyList(),
    val gridSize: Int = 4,
    val sortOrder: SortOrder = SortOrder.ASC
): ComposeState