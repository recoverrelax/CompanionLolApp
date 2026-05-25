package com.companion.lol.data.mapper

import com.companion.lol.data.DdragonImage
import com.companion.lol.data.model.ChampionDetailsModel
import com.companion.lol.data.model.other.ChampionSkin
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsTable
import com.companion.lol.storage.sqldelight.tables.SkinTable

fun ChampionDetailsTable.model(keyName: String, skins: List<SkinTable>) =
  ChampionDetailsModel(
    lore = this.lore,
    blurb = this.blurb,
    tags = this.tags,
    skins =
      skins.map {
        ChampionSkin(
          skinId = it.skinId,
          name = it.name,
          image =
            DdragonImage.championSkinImage(
              championId = this.id,
              keyName = keyName,
              skinNumber = it.number,
              skinName = it.name,
            ),
          isChroma = it.isChroma,
        )
      },
  )
