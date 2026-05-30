@file:Suppress("UNCHECKED_CAST")

package com.companion.lol.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.navigation.keys.ScreenKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

inline fun <reified S : ScreenKey> BackStackSaver(
  savedStateHandle: SavedStateHandle
): BackStackSaver<S> =
  BackStackSaver(savedStateHandle = savedStateHandle, serializer = serializer())

class BackStackSaver<S : ScreenKey>(
  savedStateHandle: SavedStateHandle,
  private val serializer: KSerializer<List<S>?>,
) {
  private var saverDelegate by
    savedStateHandle.saved<List<S>?>(
      serializer = serializer,
      key = ScreenKey.id<ScreenKey>(),
      init = { null },
    )

  fun attackBackStack(backStack: BackStack<S>, restore: Boolean) {
    (backStack as BackStack.Impl<S>).saver = { saverDelegate = it }
    if (restore) saverDelegate?.let(backStack::setHistory)
  }
}
