@file:Suppress("OPT_IN_TO_INHERITANCE")

package com.companion.lol.app.util

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface PersistedFlow<T> : StateFlow<T> {
  fun update(current: (T) -> T)
}

class MutablePersistedStateFlow<T>(
  private val flow: MutableStateFlow<T>,
  private val saver: (T) -> Unit,
) : PersistedFlow<T>, MutableStateFlow<T> by flow {

  override fun update(current: (T) -> T) =
    flow.update { current -> current(current).also { saver(it) } }
}

@Suppress("AssignedValueIsNeverRead")
inline fun <reified T> SavedStateHandle.persistedFlow(
  key: String,
  initialValue: T,
): PersistedFlow<T> {
  var saver: T by this.saved(key = key) { initialValue }
  return MutablePersistedStateFlow(flow = MutableStateFlow(saver), saver = { saver = it })
}
