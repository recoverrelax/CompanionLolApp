package com.companion.lol.app.util

import kotlin.time.Duration
import kotlin.time.measureTimedValue
import kotlinx.coroutines.delay

suspend inline fun <T> awaitAtLeast(duration: Duration, action: () -> T): T {
  if (duration == Duration.ZERO) {
    return action()
  }
  val timedValue = measureTimedValue { action() }
  delay((duration - timedValue.duration))
  return timedValue.value
}
