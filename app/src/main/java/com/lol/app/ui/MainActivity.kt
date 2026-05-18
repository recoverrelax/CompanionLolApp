package com.lol.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.unit.IntOffset
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEvent
import com.lol.app.base.material3.CompanionAppSurface
import com.lol.app.base.theme.CompanionAppTheme
import com.lol.app.navigation.BackStack
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.InitialScreenKey
import com.lol.app.navigation.LoginKey
import com.lol.app.navigation.ScreenKey
import com.lol.app.navigation.SettingsKey
import com.lol.app.navigation.rememberCompanionAppBackstack
import com.lol.app.ui.bottom_nav.NavigationBar
import com.lol.app.ui.bottom_nav.rememberNavigationBarDecoratorStrategy
import com.lol.app.ui.screens.championList.ChampionListScreen
import com.lol.app.ui.screens.login.LoginScreen
import com.lol.app.ui.screens.settings.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

val LocalContentPadding = compositionLocalOf { PaddingValues.Zero }

private val contentTransformScreenTransition =
  ContentTransform(EnterTransition.None, ExitTransition.None)

private val predictiveBack =
  ContentTransform(
    EnterTransition.None,
    scaleOut(targetScale = 0.7f) + slideOutHorizontally(targetOffsetX = { it / 2 }),
  )

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
    setContent { MainScreen() }
  }
}

@Composable
private fun MainScreen() {
  CompanionAppTheme {
    val backStack: BackStack<ScreenKey> = rememberCompanionAppBackstack(listOf(InitialScreenKey))

    val viewModel =
      hiltViewModel<MainViewModel, MainViewModel.Factory>(
        creationCallback = { factory -> factory.create(backStack) }
      )

    StatusBarEffect(backStack)

    SharedTransitionLayout {
      val navigationBarDecoratorStrategy =
        rememberNavigationBarDecoratorStrategy<ScreenKey>(
          navBar = { NavigationBar(backStack) },
          sharedTransitionScope = this,
        )

      Scaffold(containerColor = Transparent, contentColor = MaterialTheme.colorScheme.onSurface) {
        contentPadding ->
        CompositionLocalProvider(LocalContentPadding provides contentPadding) {
          NavDisplay(
            backStack = backStack.history,
            onBack = backStack::goBack,
            entryDecorators =
              rememberDecorators(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
                // rememberBottomNavEntryDecorator(backStack),
              ),
            sceneDecoratorStrategies = listOf(navigationBarDecoratorStrategy),
            transitionSpec = { contentTransformScreenTransition },
            popTransitionSpec = { contentTransformScreenTransition },
            predictivePopTransitionSpec = predictiveBack(),
          ) { key: ScreenKey ->
            NavEntry(key = key, metadata = mapOf("screen_key" to key)) {
              when (key) {
                InitialScreenKey -> PlaceHolderScreen()
                LoginKey -> LoginScreen(onLoginClicked = viewModel::onLoginClicked)
                ChampionListKey -> ChampionListScreen()
                SettingsKey -> SettingsScreen(onLogoutClicked = viewModel::onLogoutClicked)
              }
            }
          }
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
private fun StatusBarEffect(backStack: BackStack<*>) {
  val activity = checkNotNull(LocalActivity.current)
  LaunchedEffect(activity, backStack) {
    snapshotFlow { backStack.current }
      .filterNotNull()
      .distinctUntilChanged()
      .collectLatest { screenKey ->
        val window = activity.window

        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
          when (screenKey) {
            InitialScreenKey,
            LoginKey,
            ChampionListKey-> false
            SettingsKey -> true
          }
      }
  }
}

@Composable
private fun <T : ScreenKey> rememberDecorators(
  vararg decorators: NavEntryDecorator<T>
): List<NavEntryDecorator<T>> = remember { listOf(*decorators) }
