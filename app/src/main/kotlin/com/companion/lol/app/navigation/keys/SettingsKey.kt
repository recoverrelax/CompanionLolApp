package com.companion.lol.app.navigation.keys

import androidx.compose.runtime.Composable
import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.ui.screens.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsKey : ScreenKey {
  override fun requiresAuth(): Boolean = true

  override fun isNavBarEntry(): Boolean = true

  @Composable
  override fun Content(backStack: BackStack<ScreenKey>) {
    SettingsScreen()
  }
}
