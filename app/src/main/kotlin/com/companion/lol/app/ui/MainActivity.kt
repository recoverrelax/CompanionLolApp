@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.app.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.companion.lol.app.compose.ui.theme.CompanionAppTheme
import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.navigation.keys.ChampionDetailsKey
import com.companion.lol.app.navigation.keys.ChampionListKey
import com.companion.lol.app.navigation.keys.InitialScreenKey
import com.companion.lol.app.navigation.keys.LoginKey
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.navigation.keys.SettingsKey
import com.companion.lol.app.navigation.keys.entryScreenKey
import com.companion.lol.app.ui.scene.rememberBottomSheetSceneStrategy
import com.companion.lol.app.ui.scene.rememberNavigationBarDecoratorStrategy
import com.companion.lol.app.util.ChampionColorCache
import com.companion.lol.app.util.LocalChampionColorCache
import com.companion.lol.app.util.modifier.LocalSnackBarPositionReporter
import com.companion.lol.app.util.modifier.SnackBarPositionReporter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()

    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
    setContent { MainScreen() }
  }
}

@Composable
private fun MainScreen() {
  CompanionAppTheme {
    val viewModel = hiltViewModel<MainViewModel>()

    MainScreen(
      snackBarManager = viewModel.snackBarManager,
      colorCache = viewModel.colorCache,
      backStack = viewModel.backStack,
    )
  }
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
private fun MainScreen(
  snackBarManager: SnackBarManager,
  colorCache: ChampionColorCache,
  backStack: BackStack<ScreenKey>,
) {
  val posReporter = remember(backStack) { SnackBarPositionReporter(backStack) }

  CompositionLocalProvider(
    LocalSnackBarPositionReporter provides posReporter,
    LocalChampionColorCache provides colorCache,
  ) {
    Scaffold(
      containerColor = MaterialTheme.colorScheme.secondary,
      snackbarHost = { snackBarManager.SnackBarHost(posReporter) },
      contentWindowInsets = WindowInsets(),
    ) {
      NavDisplay(backStack = backStack)
    }
  }
}

@Composable
private fun NavDisplay(backStack: BackStack<ScreenKey>) {
  val navigationBar =
    @Composable { NavigationBar(currentKey = { backStack.current }, goTo = backStack::goTo) }

  SharedTransitionLayout {
    NavDisplay(
      backStack = backStack.history,
      onBack = backStack::goBack,
      entryDecorators =
        listOf(
          rememberSaveableStateHolderNavEntryDecorator(),
          rememberViewModelStoreNavEntryDecorator(),
        ),
      sceneStrategies = listOf(rememberBottomSheetSceneStrategy()),
      sceneDecoratorStrategies =
        listOf(
          rememberNavigationBarDecoratorStrategy(
            navBar = navigationBar,
            sharedTransitionScope = this,
          )
        ),
      entryProvider =
        entryProvider {
          entryScreenKey<InitialScreenKey>()
          entryScreenKey<LoginKey>()
          entryScreenKey<ChampionListKey>()
          entryScreenKey<ChampionDetailsKey>()
          entryScreenKey<SettingsKey>()
        },
    )
  }
}
