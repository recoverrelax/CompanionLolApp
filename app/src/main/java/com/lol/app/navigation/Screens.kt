package com.lol.app.navigation

import androidx.compose.runtime.Stable
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.lol.app.navigation.serializer.ChampionIdSerializer
import kotlinx.serialization.Serializable

@Stable @Serializable sealed interface ScreenKey

@Serializable data object InitialScreenKey : ScreenKey

@Serializable data object LoginKey : ScreenKey

@Serializable data object ChampionListKey : ScreenKey

@Serializable
data class ChampionDetailsKey (
  @Serializable(ChampionIdSerializer::class)
  val championId: ChampionId
) : ScreenKey {
  // we do not allow multiple instances, even with different ids
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass == other?.javaClass) return true

    return false
  }

  override fun hashCode(): Int = championId.hashCode()
}

@Serializable data object SettingsKey : ScreenKey
