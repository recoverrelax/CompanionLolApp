package com.companion.lol.app.ui.scene

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.get
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.util.cacheSize

data class NavigationBarDecoratorScene<T : Any>(
  private val scene: Scene<T>,
  private val sharedTransitionScope: SharedTransitionScope,
  private val navBarContent: @Composable (() -> Unit),
) : Scene<T> by scene {
  override val key = scene::class to scene.key

  override val content =
    @Composable {
      val animatedContentScope = LocalNavAnimatedContentScope.current
      val isMovableContentCaller =
        animatedContentScope.transition.targetState == EnterExitState.Visible

      with(sharedTransitionScope) {
        Column(Modifier.fillMaxSize()) {
          Box(modifier = Modifier.weight(1f)) { scene.content() }
          Box(
            modifier =
              Modifier.cacheSize(!isMovableContentCaller)
                .sharedElement(rememberSharedContentState("nav-bar"), animatedContentScope)
          ) {
            if (isMovableContentCaller) {
              navBarContent()
            }
          }
        }
      }
    }
}

@Composable
fun <T : Any> rememberNavigationBarDecoratorStrategy(
  navBar: @Composable () -> Unit,
  sharedTransitionScope: SharedTransitionScope,
): NavigationBarDecoratorStrategy<T> {
  val currentNavBar by rememberUpdatedState(navBar)

  val movableNavBar = remember { movableContentOf { currentNavBar() } }

  return remember(sharedTransitionScope) {
    NavigationBarDecoratorStrategy(sharedTransitionScope, movableNavBar)
  }
}

class NavigationBarDecoratorStrategy<T : Any>(
  private val sharedTransitionScope: SharedTransitionScope,
  private val navBarContent: @Composable () -> Unit,
) : SceneDecoratorStrategy<T> {

  override fun SceneDecoratorStrategyScope<T>.decorateScene(scene: Scene<T>): Scene<T> {
    val lastEntry: NavEntry<T> = scene.entries.firstOrNull() ?: return scene

    val key: ScreenKey = lastEntry.metadata[ScreenKey.Companion.Id] ?: return scene

    return if (key.isNavBarEntry()) {
      NavigationBarDecoratorScene(scene, sharedTransitionScope, navBarContent)
    } else (scene)
  }
}
