package com.companion.lol.app.util.modifier

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.navigation.keys.ScreenKey
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@Stable
/**
 * This idea is kinda stupid. Its really not that easy to position the snackbar in the parent
 * composable and be aware of the NavEntry content so that it does not overlap anything. This idea
 * works but its kinda hack :p Otherwise we would need a host on every NavEntry, and if we want to
 * display an error and change screen it will not be present full time
 *
 * TODO() find better way
 */
class SnackBarPositionReporter(private val backStack: BackStack<ScreenKey>) {
  private val positions = mutableStateMapOf<String, Int>()

  fun updatePosition(screenId: String, position: Int) {
    if (positions[screenId] != position) {
      positions[screenId] = position
    }
  }

  val translationY =
    snapshotFlow {
        val current = backStack.current
        val keyId = ScreenKey.id(current::class)
        positions[keyId]?.toFloat()
      }
      .filterNotNull()
      .map { -it }
}

val LocalSnackBarPositionReporter =
  staticCompositionLocalOf<SnackBarPositionReporter> { error("not provided") }

@Stable
enum class SnackBarPosition {
  TOP,
  BOTTOM,
}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
inline fun <reified S : ScreenKey> Modifier.reportSnackBarPosition(
  position: SnackBarPosition = SnackBarPosition.TOP
): Modifier = reportSnackBarPosition(position = position, screenId = ScreenKey.id<S>())

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun Modifier.reportSnackBarPosition(position: SnackBarPosition, screenId: String): Modifier =
  this.then(SaveLayoutTopElement(screenId, position))

private data class SaveLayoutTopElement(
  private val screenId: String,
  private val position: SnackBarPosition,
) : ModifierNodeElement<SaveLayoutTopNode>() {
  override fun create() = SaveLayoutTopNode(screenId, position)

  override fun update(node: SaveLayoutTopNode) {}

  override fun InspectorInfo.inspectableProperties() {
    name = "reportSnackBarPosition"
    properties["screenId"] = screenId
    properties["position"] = position
  }
}

private class SaveLayoutTopNode(
  private val screenId: String,
  private val position: SnackBarPosition,
) : Modifier.Node(), GlobalPositionAwareModifierNode, CompositionLocalConsumerModifierNode {
  private var lastValue = Float.NaN

  override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
    if (!isAttached) return
    val positionInLocal = coordinates.positionInRoot().y

    if (positionInLocal != lastValue) {
      lastValue = positionInLocal

      val value =
        when (position) {
          SnackBarPosition.TOP -> positionInLocal
          SnackBarPosition.BOTTOM -> positionInLocal + coordinates.size.height
        }
      val root = coordinates.findRootCoordinates().size.height
      val distance = root - value

      currentValueOf(LocalSnackBarPositionReporter).updatePosition(screenId, distance.roundToInt())
    }
  }
}
