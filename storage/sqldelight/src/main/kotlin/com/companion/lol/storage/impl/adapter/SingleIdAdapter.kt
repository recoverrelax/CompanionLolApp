package com.companion.lol.storage.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.companion.lol.storage.impl.model.ids.SingleId

object SingleIdAdapter : ColumnAdapter<SingleId, Long> {
  override fun decode(databaseValue: Long): SingleId = SingleId

  override fun encode(value: SingleId): Long {
    return SingleId.RAW_DB_ID
  }
}
