package com.companion.lol.storage.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.companion.lol.storage.impl.model.other.ChampionTag

object ChampionTagAdapter : ColumnAdapter<List<ChampionTag>, String> {
  override fun decode(databaseValue: String): List<ChampionTag> {
    if (databaseValue.isEmpty()) return emptyList()

    return databaseValue.split(",").map { ChampionTag.from(it) }
  }

  override fun encode(value: List<ChampionTag>): String {
    return value.joinToString(",") { it.server }
  }
}
