package com.companion.lol.app.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.companion.lol.app.io.UiError
import com.companion.lol.app.util.modifier.SnackBarPositionReporter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

@Stable
interface SnackBarManager : MessagePoster {
  @Composable
  fun SnackBarHost(
    positionReporter: SnackBarPositionReporter,
    snackBar: @Composable (SnackbarData) -> Unit = {
      Snackbar(
        snackbarData = it,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onBackground,
      )
    },
  )

  class Impl : SnackBarManager {
    @Stable private val snackBarHostState: SnackbarHostState = SnackbarHostState()
    private val _message = MutableStateFlow(emptyList<UiError>())
    val message: Flow<UiError> =
      _message.map { it.firstOrNull() }.distinctUntilChanged().filterNotNull()

    override fun emitMessage(message: UiError) {
      _message.update { it + message }
    }

    private fun clearMessage(id: Long) {
      _message.update { messages -> messages.filterNot { it.id == id } }
    }

    @Composable
    override fun SnackBarHost(
      positionReporter: SnackBarPositionReporter,
      snackBar: @Composable ((SnackbarData) -> Unit),
    ) {
      val translationY by positionReporter.translationY.collectAsState(0f)
      val animatedTranslationY = animateFloatAsState(translationY)

      SnackbarHost(
        modifier = Modifier.graphicsLayer { this.translationY = animatedTranslationY.value },
        hostState = snackBarHostState,
        snackbar = snackBar,
      )

      LaunchedEffect(Unit) {
        message.collectLatest { error ->
          snackBarHostState.showSnackbar(error.message, duration = SnackbarDuration.Short)
          clearMessage(error.id)
        }
      }
    }
  }
}

@Stable
interface MessagePoster {
  fun emitMessage(message: UiError)
}
