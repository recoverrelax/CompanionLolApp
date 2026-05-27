package com.companion.lol.storage.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.companion.lol.storage.impl.model.ids.PartyTypeId

object PartyTypeIdAdapter : ColumnAdapter<PartyTypeId, Long> {
  override fun decode(databaseValue: Long): PartyTypeId {
    return PartyTypeId(databaseValue.toInt())
  }

  override fun encode(value: PartyTypeId): Long {
    return value.value.toLong()
  }
}
