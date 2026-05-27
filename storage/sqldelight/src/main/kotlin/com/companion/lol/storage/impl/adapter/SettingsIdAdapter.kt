package com.companion.lol.storage.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.companion.lol.storage.impl.model.ids.SINGLE_ID
import com.companion.lol.storage.impl.model.ids.SettingsId

object SettingsIdAdapter : ColumnAdapter<SettingsId, Long> {
  override fun decode(databaseValue: Long): SettingsId {
    return SettingsId
  }

  override fun encode(value: SettingsId): Long {
    return SINGLE_ID
  }
}
