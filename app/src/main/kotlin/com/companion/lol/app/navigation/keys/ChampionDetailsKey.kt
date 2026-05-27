@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.app.navigation.keys

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.navigation.serializer.ChampionIdSerializer
import com.companion.lol.app.ui.screens.championDetails.ChampionDetailsScreen
import com.companion.lol.storage.impl.model.ids.ChampionId
import kotlinx.serialization.Serializable

@Serializable
data class ChampionDetailsKey(
  @Serializable(ChampionIdSerializer::class) val championId: ChampionId
) : ScreenKey {

  override fun requiresAuth(): Boolean = true

  override fun type(): ScreenKey.Type = ScreenKey.Type.BottomSheet()

  @Composable
  override fun Content(backStack: BackStack<ScreenKey>) {
    ChampionDetailsScreen(championId = championId, goBack = backStack::goBack)
  }
}
