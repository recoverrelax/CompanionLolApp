package com.companion.lol.storage.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.companion.lol.storage.impl.model.ids.SkinId

object SkinIdAdapter : ColumnAdapter<SkinId, Long> {
  override fun decode(databaseValue: Long): SkinId {
    return SkinId(databaseValue.toInt())
  }

  override fun encode(value: SkinId): Long {
    return value.value.toLong()
  }
}
