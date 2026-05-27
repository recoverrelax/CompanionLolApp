package com.companion.lol.data.mapper

import com.companion.lol.data.io.images.DdragonImage
import com.companion.lol.data.model.ChampionModel
import com.companion.lol.storage.sqldelight.tables.ChampionWithFavoritesView

fun ChampionWithFavoritesView.model() =
  ChampionModel(
    id = this.id,
    name = this.name,
    keyName = this.keyName,
    title = this.title,
    squareImage = DdragonImage.Square(this.squareImageName),
    partyType = this.partyType,
    isFavorite = this.isFavorite ?: false,
  )
