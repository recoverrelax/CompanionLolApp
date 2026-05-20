@file:OptIn(ExperimentalMaterial3Api::class)

package com.lol.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.unit.IntOffset
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEvent
import com.lol.app.base.material3.CompanionAppSurface
import com.lol.app.base.theme.CompanionAppTheme
import com.lol.app.navigation.BackStack
import com.lol.app.navigation.ChampionDetailsKey
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.InitialScreenKey
import com.lol.app.navigation.LoginKey
import com.lol.app.navigation.ScreenKey
import com.lol.app.navigation.SettingsKey
import com.lol.app.navigation.entryScreenKey
import com.lol.app.ui.scene.BottomSheetSceneStrategy
import com.lol.app.ui.scene.rememberBottomSheetSceneStrategy
import com.lol.app.ui.scene.rememberNavigationBarDecoratorStrategy
import com.lol.app.ui.screens.NavigationBar
import com.lol.app.ui.screens.NavigationBarScreen
import com.lol.app.ui.screens.championDetails.ChampionDetailsScreen
import com.lol.app.ui.screens.championList.ChampionListScreen
import com.lol.app.ui.screens.login.LoginScreen
import com.lol.app.ui.screens.settings.SettingsScreen
import com.lol.app.util.LocalChampionColorCache
import dagger.hilt.android.AndroidEntryPoint

val LocalContentPadding = compositionLocalOf { PaddingValues.Zero }

private val contentTransformScreenTransition =
  ContentTransform(EnterTransition.None, ExitTransition.None)

fun <T : Any> predictiveBack():
  AnimatedContentTransitionScope<Scene<T>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform = {
  val spring =
    spring(stiffness = Spring.StiffnessVeryLow, visibilityThreshold = IntOffset(100, 100))

  ContentTransform(
    EnterTransition.None,
    if (it == NavigationEvent.EDGE_RIGHT) {
      scaleOut(targetScale = 0.9f) + slideOutHorizontally(spring, targetOffsetX = { -(it / 2) })
    } else {
      scaleOut(targetScale = 0.9f) + slideOutHorizontally(spring, targetOffsetX = { it / 2 })
    },
  )
}

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
    val backStack: BackStack<ScreenKey> = viewModel.backStack

    val bottomSheetStrategy = rememberBottomSheetSceneStrategy<ScreenKey>()

    SharedTransitionLayout {
      val navigationBarDecoratorStrategy =
        rememberNavigationBarDecoratorStrategy<ScreenKey>(
          navBar = { NavigationBar(backStack = backStack) },
          sharedTransitionScope = this,
        )

      Scaffold(containerColor = Transparent, contentColor = MaterialTheme.colorScheme.onSurface) {
        contentPadding ->
        CompositionLocalProvider(
          LocalContentPadding provides contentPadding,
          LocalChampionColorCache provides viewModel.colorCache,
        ) {
          NavDisplay(
            backStack = backStack.history,
            onBack = backStack::goBack,
            entryDecorators =
              rememberDecorators(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
              ),
            sceneStrategies = listOf(bottomSheetStrategy),
            sceneDecoratorStrategies = listOf(navigationBarDecoratorStrategy),
            transitionSpec = { contentTransformScreenTransition },
            popTransitionSpec = { contentTransformScreenTransition },
            predictivePopTransitionSpec = predictiveBack(),
            entryProvider =
              entryProvider {
                entryScreenKey<InitialScreenKey> { PlaceHolderScreen() }
                entryScreenKey<LoginKey> { LoginScreen(onLoginClicked = viewModel::onLoginClicked) }
                entryScreenKey<ChampionListKey>(metadata = NavigationBarScreen.metadata()) {
                  ChampionListScreen(onCardClick = viewModel::goToChampionDetails)
                }
                entryScreenKey<SettingsKey>(metadata = NavigationBarScreen.metadata()) {
                  SettingsScreen(onLogoutClicked = viewModel::onLogoutClicked)
                }

                entryScreenKey<ChampionDetailsKey>(
                  metadata = BottomSheetSceneStrategy.bottomSheet()
                ) {
                  ChampionDetailsScreen(it.championId)
                }
              },
          )
        }
      }
    }
  }
}

@Composable
private fun PlaceHolderScreen() {
  CompanionAppSurface(modifier = Modifier.fillMaxSize()) {}
}

@Composable
private fun <T : ScreenKey> rememberDecorators(
  vararg decorators: NavEntryDecorator<T>
): List<NavEntryDecorator<T>> = remember { listOf(*decorators) }
