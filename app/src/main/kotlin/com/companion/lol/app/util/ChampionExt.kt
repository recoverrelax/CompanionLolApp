package com.companion.lol.app.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.companion.lol.app.R
import com.companion.lol.storage.impl.model.other.ChampionTag
import com.companion.lol.storage.impl.model.other.GridSize
import com.companion.lol.storage.impl.model.other.PartyType
import com.companion.lol.storage.impl.model.other.SortOrder

val GridSize.count: Int
  get() =
    when (this) {
      GridSize.SMALL -> 5
      GridSize.MEDIUM -> 4
      GridSize.LARGE -> 3
    }

fun GridSize.next(): GridSize = GridSize.entries.run { get((ordinal + 1) % size) }

val PartyType.color: Color
  get() =
    when (this) {
      PartyType.MANA -> Color(0xFF2196F3)
      PartyType.HEAT -> Color(0xFFFF5722)
      PartyType.FURY -> Color(0xFFF4511E)
      PartyType.FEROCITY -> Color(0xFFF4511E)
      PartyType.RAGE -> Color(0xFFE64A19)
      PartyType.BLOOD_WELL -> Color(0xFFD84315)
      PartyType.CRIMSON_RUSH -> Color(0xFF546E7A)
      PartyType.ENERGY -> Color(0xFFFFCA28)
      PartyType.SHIELD -> Color(0xFFFFB300)
      PartyType.FLOW -> Color(0xFF4DB6AC)
      PartyType.COURAGE -> Color(0xFF9CCC65)
      PartyType.GRIT -> Color(0xFF9C27B0)
      PartyType.NONE -> Color(0xFF212121)
    }

val ChampionTag.icon: Int
  @DrawableRes
  get() =
    when (this) {
      ChampionTag.MARKSMAN -> R.drawable.class_marksman
      ChampionTag.SUPPORT -> R.drawable.class_support
      ChampionTag.MAGE -> R.drawable.class_mage
      ChampionTag.ASSASSIN -> R.drawable.class_assassin
      ChampionTag.FIGHTER -> R.drawable.class_fighter
      ChampionTag.TANK -> R.drawable.class_tank
    }

fun SortOrder.toggle() =
  when (this) {
    SortOrder.FAVORITES -> SortOrder.ASC
    SortOrder.ASC -> SortOrder.FAVORITES
  }
