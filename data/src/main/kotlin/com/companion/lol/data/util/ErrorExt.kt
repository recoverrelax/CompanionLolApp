@file:OptIn(ExperimentalContracts::class)

package com.companion.lol.data.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CancellationException

inline fun <R, T : R> Result<T>.getOrPropagate(
  shouldRethrow: (e: Throwable) -> Boolean = { it is CancellationException },
  onFailure: (exception: Throwable) -> R,
): R {
  contract { callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE) }
  return getOrElse(
    onFailure = { e ->
      if (shouldRethrow(e)) {
        throw e
      } else {
        onFailure(e)
      }
    }
  )
}
