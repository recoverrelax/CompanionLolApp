package com.companion.lol.storage.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.companion.lol.storage.impl.model.ids.ChampionId

object ChampionIdAdapter : ColumnAdapter<ChampionId, Long> {
  override fun decode(databaseValue: Long): ChampionId {
    return ChampionId(databaseValue.toInt())
  }

  override fun encode(value: ChampionId): Long {
    return value.value.toLong()
  }
}
