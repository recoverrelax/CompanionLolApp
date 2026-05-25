package com.companion.lol.app.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.companion.lol.app.io.UiError
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Stable
interface SnackBarManager {
  @Stable val snackBarHostState: SnackbarHostState

  suspend fun addError(error: UiError)

  @Composable fun ShowSnackBarMessagesEffect(error: suspend SnackbarHostState.(UiError) -> Unit)
}

private class Impl(override val snackBarHostState: SnackbarHostState) : SnackBarManager {
  // We want a maximum of 3 errors queued
  private val pendingErrors = Channel<UiError>(3, BufferOverflow.DROP_OLDEST)

  @Composable
  override fun ShowSnackBarMessagesEffect(error: suspend SnackbarHostState.(UiError) -> Unit) {
    LaunchedEffect(Unit) {
      pendingErrors.receiveAsFlow().collect { error -> error(snackBarHostState, error) }
    }
  }

  /** Add [error] to the queue of errors to display. */
  override suspend fun addError(error: UiError) {
    pendingErrors.send(error)
  }
}

@Composable
fun rememberSnackBarManager(): SnackBarManager {
  return remember { Impl(SnackbarHostState()) }
}
