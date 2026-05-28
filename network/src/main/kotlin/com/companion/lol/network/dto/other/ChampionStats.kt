package com.companion.lol.network.dto.other

import android.annotation.SuppressLint
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
@SuppressLint("UnsafeOptInUsageError")
@Suppress("unused", "SpellCheckingInspection")
class ChampionStats(
  val hp: Int,
  val hpperlevel: Int,
  val mp: Int,
  val mpperlevel: Double,
  val movespeed: Int,
  val armor: Int,
  val armorperlevel: Float,
  val spellblock: Int,
  val spellblockperlevel: Float,
  val attackrange: Int,
  val hpregen: Float,
  val hpregenperlevel: Float,
  val mpregen: Float,
  val mpregenperlevel: Float,
  val crit: Int,
  val critperlevel: Int,
  val attackdamage: Int,
  val attackdamageperlevel: Float,
  val attackspeedperlevel: Float,
  val attackspeed: Float,
)
