package com.lol.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.InitialScreenKey
import com.lol.app.navigation.LoginKey
import com.lol.app.navigation.MainBottomNavScreen
import com.lol.app.navigation.ScreenKey
import com.lol.app.navigation.SettingsKey
import com.lol.app.ui.screens.championList.ChampionListScreen
import com.lol.app.ui.screens.login.LoginScreen
import com.lol.app.ui.screens.settings.SettingsScreen
import com.lol.app.ui.theme.CompanionAppTheme
import dagger.hilt.android.AndroidEntryPoint

private val contentTransformScreenTransition =
  ContentTransform(EnterTransition.None, ExitTransition.None)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      CompanionAppTheme {
        // viewModel not stable because it is read from outside
        val backStack = remember(viewModel) { viewModel.backStack }

        Scaffold(
          bottomBar = {
            val currentKey = backStack.current

            if (currentKey is MainBottomNavScreen) {
              NavigationBar {
                NavigationBarItem(
                  icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                  label = { Text("Feature") },
                  selected = currentKey is ChampionListKey,
                  onClick = { backStack.goTo(ChampionListKey) },
                )

                NavigationBarItem(
                  icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                  label = { Text("Settings") },
                  selected = currentKey is SettingsKey,
                  onClick = { backStack.goTo(SettingsKey) },
                )
              }
            }
          }
        ) { contentPadding ->
          val modifier = Modifier.padding(contentPadding)

          NavDisplay(
            backStack = backStack.history,
            onBack = backStack::goBack,
            entryDecorators =
              listOf(
                // Required for saving Compose state per entry
                rememberSaveableStateHolderNavEntryDecorator(),
                // Required for ViewModel scoping per entry
                rememberViewModelStoreNavEntryDecorator(),
              ),
            transitionSpec = { contentTransformScreenTransition },
            popTransitionSpec = { contentTransformScreenTransition },
            predictivePopTransitionSpec = { contentTransformScreenTransition },
          ) { key: ScreenKey ->
            NavEntry(key) {
              when (key) {
                InitialScreenKey -> PlaceHolderScreen(modifier = modifier)
                LoginKey ->
                  LoginScreen(modifier = modifier, onLoginClicked = viewModel::onLoginClicked)
                ChampionListKey -> ChampionListScreen(modifier = modifier)
                SettingsKey ->
                  SettingsScreen(modifier = modifier, onLogoutClicked = viewModel::onLogoutClicked)
              }
            }
          }
        }
      }
    }
  }

  @Composable
  private fun PlaceHolderScreen(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary))
  }
}
