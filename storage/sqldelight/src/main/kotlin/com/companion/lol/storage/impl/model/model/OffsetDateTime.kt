package com.companion.lol.storage.impl.model.model

import kotlin.time.Clock
import kotlin.time.Duration
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class OffsetDateTime(val dateTime: LocalDateTime, val offset: UtcOffset) {

  operator fun minus(other: OffsetDateTime): Duration {
    val thisInstant = dateTime.toInstant(offset)
    val otherInstant = other.dateTime.toInstant(other.offset)

    return thisInstant - otherInstant
  }

  companion object {
    fun now(): OffsetDateTime {
      val instant = Clock.System.now()
      val timeZone = TimeZone.currentSystemDefault()

      return OffsetDateTime(
        dateTime = instant.toLocalDateTime(timeZone),
        offset = timeZone.offsetAt(instant),
      )
    }
  }
}
