package com.companion.lol.util

import kotlinx.coroutines.CancellationException
import timber.log.Timber

inline fun <T> catchIoException(action: () -> T): T? {
  return try {
    action()
  } catch (e: Throwable) {
    when (e) {
      is CancellationException -> throw e
      else -> {
        Timber.e(e)
        return null
      }
    }
  }
}
