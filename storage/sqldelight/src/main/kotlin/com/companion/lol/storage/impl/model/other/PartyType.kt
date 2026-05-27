package com.companion.lol.storage.impl.model.other

import com.companion.lol.storage.impl.model.ids.PartyTypeId

enum class PartyType(val server: String, val label: String) {
  MANA("Mana", label = "Mana"),
  CRIMSON_RUSH("Crimson_Rush", label = "Crimson Rush"),
  FURY("Fury", label = "Fury"),
  ENERGY("Energy", label = "Energy"),
  HEAT("Heat", label = "Heat"),
  SHIELD("Shield", label = "Shield"),
  FEROCITY("Ferocity", label = "Ferocity"),
  RAGE("Rage", label = "Rage"),
  FLOW("Flow", label = "Flow"),
  COURAGE("Courage", label = "Courage"),
  BLOOD_WELL("Blood_Well", label = "Blood Well"),
  GRIT("Grit", label = "Grit"),
  NONE("None", label = "None");

  companion object {
    fun from(server: String?) = entries.firstOrNull { it.server == server } ?: NONE

    fun from(partyTypeId: PartyTypeId): PartyType {
      return PartyType.entries.first { it.ordinal == partyTypeId.value }
    }
  }

  val dbId: PartyTypeId
    get() = PartyTypeId(this.ordinal)
}
