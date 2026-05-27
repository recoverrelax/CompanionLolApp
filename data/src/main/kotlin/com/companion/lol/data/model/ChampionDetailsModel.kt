package com.companion.lol.data.model

import androidx.compose.runtime.Stable
import com.companion.lol.data.model.other.ChampionSkin
import com.companion.lol.storage.impl.model.other.ChampionTag

@Stable
data class ChampionDetailsModel(
  val lore: String,
  val blurb: String,
  val tags: List<ChampionTag>,
  val skins: List<ChampionSkin>,
)
