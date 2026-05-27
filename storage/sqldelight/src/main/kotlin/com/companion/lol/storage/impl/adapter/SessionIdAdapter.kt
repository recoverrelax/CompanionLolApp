package com.companion.lol.storage.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.companion.lol.storage.impl.model.ids.SINGLE_ID
import com.companion.lol.storage.impl.model.ids.SessionId

object SessionIdAdapter : ColumnAdapter<SessionId, Long> {
  override fun decode(databaseValue: Long): SessionId {
    return SessionId
  }

  override fun encode(value: SessionId): Long {
    return SINGLE_ID
  }
}
