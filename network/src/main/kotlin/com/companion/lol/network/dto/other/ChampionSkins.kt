package com.companion.lol.network.dto.other

import android.annotation.SuppressLint
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
@SuppressLint("UnsafeOptInUsageError")
class ChampionSkins(
  val id: Int,
  val num: Int,
  val name: String,
  val chromas: Boolean,
  val parentSkin: Int? = null,
)
