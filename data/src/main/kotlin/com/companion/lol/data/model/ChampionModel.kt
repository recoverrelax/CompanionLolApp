package com.companion.lol.data.model

import androidx.compose.runtime.Stable
import com.companion.lol.data.io.images.DdragonImage
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.companion.lol.storage.impl.model.other.PartyType

@Stable
data class ChampionModel(
  val id: ChampionId,
  val name: String,
  val keyName: String,
  val title: String,
  val squareImage: DdragonImage.Square,
  val partyType: PartyType,
  val isFavorite: Boolean,
)
