package com.companion.lol.app.compose.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent

fun <T : Any> defaultNoTransition():
  AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
  ContentTransform(EnterTransition.None, ExitTransition.None)
}

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
