package com.companion.lol.network.dto

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.companion.lol.network.dto.other.ChampionImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Keep
@Suppress("CanBeParameter", "SpellCheckingInspection")
@SuppressLint("UnsafeOptInUsageError")
class ChampionDto(
  private val key: String,
  val id: String,
  val name: String,
  val title: String,
  val image: ChampionImage,
  @SerialName("partype") val partType: String,
) {
  @Transient val championKey: Int = key.toInt()
}
