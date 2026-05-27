package com.companion.lol.app.util

import com.companion.lol.data.model.ChampionModel
import com.companion.lol.storage.impl.model.other.SortOrder

fun List<ChampionModel>.sortBy(order: SortOrder): List<ChampionModel> {
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
