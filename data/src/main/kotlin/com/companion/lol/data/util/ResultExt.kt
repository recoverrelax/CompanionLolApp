package com.companion.lol.data.util

import kotlin.time.Duration
import kotlinx.coroutines.delay

/**
 * Retries the given [action] up to [times] times if it fails.
 *
 * @param times The number of retries.
 * @param delayDuration The duration to wait between retries.
 * @param action The suspend action to execute.
 * @return The result of the last attempt.
 */
suspend inline fun <T> withRetry(
  times: Int,
  delayDuration: Duration,
  action: suspend () -> Result<T>,
): Result<T> {
  require(times >= 0) { "times must be >= 0" }
  var attempts = 0
  while (true) {
    val result = action()
    if (result.isSuccess || attempts >= times) {
      return result
    }
    attempts++
    delay(delayDuration)
  }
}
