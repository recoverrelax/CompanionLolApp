package com.lol.app.base.material3

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import com.lol.app.ui.LocalContentPadding

val companionAppGradient: Brush
  @Stable
  @Composable
  get() =
    Brush.verticalGradient(
      colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary)
    )

val companionAppGradientInverted: Brush
  @Stable
  @Composable
  get() =
    Brush.verticalGradient(
      colors = listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.secondary)
    )

@Composable
@NonRestartableComposable
fun CompanionAppSurface(
  modifier: Modifier = Modifier,
  shape: Shape = RectangleShape,
  contentColor: Color = MaterialTheme.colorScheme.onBackground,
  border: BorderStroke? = null,
  insetStatusBar: Boolean = true,
  insetNavigationBar: Boolean = true,
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(LocalContentColor provides contentColor) {
    Box(
      modifier =
        modifier
          .then(if (border != null) Modifier.border(border, shape) else Modifier)
          .background(brush = companionAppGradient, shape = shape)
          .then(
            if (insetStatusBar) {
              Modifier.padding(top = LocalContentPadding.current.calculateTopPadding())
            } else Modifier
          )
          .then(
            if (insetNavigationBar) {
              Modifier.padding(bottom = LocalContentPadding.current.calculateBottomPadding())
            } else Modifier
          )
          .clip(shape)
          .semantics(mergeDescendants = false) {
            // TODO(b/347038246): replace `isContainer` with `isTraversalGroup` with new
            // pruning API.
            @Suppress("DEPRECATION")
            isContainer = true
          }
          .pointerInput(Unit) {},
      propagateMinConstraints = true,
    ) {
      content()
    }
  }
}
