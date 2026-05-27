package com.companion.lol.storage.impl.model.other

import com.companion.lol.storage.impl.model.ids.TagId

enum class ChampionTag(val server: String, val dbId: TagId) {
  MARKSMAN("Marksman", TagId(0)),
  SUPPORT("Support", TagId(1)),
  MAGE("Mage", TagId(2)),
  ASSASSIN("Assassin", TagId(3)),
  FIGHTER("Fighter", TagId(4)),
  TANK("Tank", TagId(5));

  val label: String = server

  companion object {
    fun from(server: String) = entries.first { it.server == server }

    fun from(dbId: TagId) = entries.first { it.dbId == dbId }
  }
}
