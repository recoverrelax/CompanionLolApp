package com.lol.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata
import com.companion.lol.app.R
import com.lol.app.base.CompanionAppPreview
import com.lol.app.base.CompanionAppPreviewWrapperProvider
import com.lol.app.base.material3.companionAppGradient
import com.lol.app.navigation.BackStack
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.ScreenKey
import com.lol.app.navigation.SettingsKey

object NavigationBarScreen : NavMetadataKey<Boolean> {
  fun metadata() = metadata { put(NavigationBarScreen, true) }
}

@Composable
fun NavigationBar(modifier: Modifier = Modifier, backStack: BackStack<ScreenKey>) {
  val screenKey = backStack.current

  NavigationBar(
    containerColor = Color.Transparent,
    modifier = modifier.background(brush = companionAppGradient),
  ) {
    val colors =
      NavigationBarItemDefaults.colors(
        selectedTextColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
        unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
        indicatorColor = MaterialTheme.colorScheme.primary,
      )

    NavigationBarItem(
      icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
      label = { Text(stringResource(R.string.champion_rotation)) },
      selected = screenKey is ChampionListKey,
      colors = colors,
      onClick = dropUnlessResumed { backStack.goTo(ChampionListKey) },
    )

    NavigationBarItem(
      icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
      label = { Text(stringResource(R.string.settings)) },
      selected = screenKey is SettingsKey,
      colors = colors,
      onClick = dropUnlessResumed { backStack.goTo(SettingsKey) },
    )
  }
}

@Composable
@CompanionAppPreview
@PreviewWrapper(CompanionAppPreviewWrapperProvider::class)
private fun Preview() {
  NavigationBar(
    modifier = Modifier.fillMaxWidth(),
    backStack =
      object : BackStack<ScreenKey> {
        override val history: List<ScreenKey> = listOf()
        override val current: ScreenKey = ChampionListKey

        override fun goTo(key: ScreenKey) = Unit

        override fun setHistory(singleKey: ScreenKey) = Unit

        override fun setHistory(newHistory: List<ScreenKey>) = Unit

        override fun goBack(): Boolean = true
      },
  )
}
