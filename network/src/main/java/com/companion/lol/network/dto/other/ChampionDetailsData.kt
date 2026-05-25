package com.companion.lol.network.dto.other

import android.annotation.SuppressLint
import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
@Suppress("SpellCheckingInspection", "unused")
@SuppressLint("UnsafeOptInUsageError")
class ChampionDetailsData(
  val key: String,
  val title: String,
  val lore: String,
  val blurb: String,
  val tags: List<String>,
  @SerialName("partytype") val partyType: String? = null,
  val stats: ChampionStats,
  val skins: List<ChampionSkins>,
)
