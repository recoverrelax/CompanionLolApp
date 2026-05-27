package com.companion.lol.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import com.companion.lol.app.R
import com.companion.lol.app.compose.app.companionAppGradient
import com.companion.lol.app.navigation.keys.ChampionListKey
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.navigation.keys.SettingsKey

@Composable
fun NavigationBar(
  modifier: Modifier = Modifier,
  currentKey: () -> ScreenKey,
  goTo: (ScreenKey) -> Unit,
) {
  val colors =
    NavigationBarItemDefaults.colors(
      selectedTextColor = MaterialTheme.colorScheme.primary,
      unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
      unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
      indicatorColor = MaterialTheme.colorScheme.primary,
    )

  NavigationBar(
    containerColor = Color.Transparent,
    modifier = modifier.background(brush = companionAppGradient),
  ) {
    CompanionLolNavigationBarItem(
      icon = Icons.AutoMirrored.Filled.List,
      label = stringResource(R.string.champion_list),
      selected = { currentKey() is ChampionListKey },
      colors = colors,
      onClick = { goTo(ChampionListKey) },
    )

    CompanionLolNavigationBarItem(
      icon = Icons.Filled.Settings,
      label = stringResource(R.string.settings),
      selected = { currentKey() is SettingsKey },
      colors = colors,
      onClick = { goTo(SettingsKey) },
    )
  }
}

@Composable
private fun RowScope.CompanionLolNavigationBarItem(
  icon: ImageVector,
  label: String,
  selected: () -> Boolean,
  colors: NavigationBarItemColors,
  onClick: () -> Unit,
) {
  NavigationBarItem(
    icon = { Icon(icon, contentDescription = null) },
    label = { Text(label) },
    selected = selected(),
    colors = colors,
    onClick = dropUnlessResumed(block = onClick),
  )
}
