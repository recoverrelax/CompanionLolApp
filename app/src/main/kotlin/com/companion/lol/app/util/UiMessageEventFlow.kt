@file:OptIn(ExperimentalForInheritanceCoroutinesApi::class)

package com.companion.lol.app.util

import androidx.compose.runtime.Stable
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@Stable
interface UiMessageEventFlow<T> : Flow<T> {
  class Impl<T>(
    private val flow: MutableSharedFlow<T> = MutableSharedFlow(extraBufferCapacity = 1)
  ) : UiMessageEventFlow<T>, MutableSharedFlow<T> by flow
}
