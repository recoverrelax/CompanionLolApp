package com.companion.lol.data.io.images

import androidx.compose.runtime.Immutable
import com.companion.lol.network.EndPoints

@Immutable
sealed interface DdragonImage {
  val imageUrl: String

  @Immutable
  data class Square(private val image: String) : DdragonImage {
    override val imageUrl: String = EndPoints.DDragon.championSquareAsset(image)
  }

  @Immutable
  data class Skin(val skinNumber: Int, val skinName: String, val keyName: String) : DdragonImage {
    override val imageUrl: String = EndPoints.DDragon.championSkinAsset(keyName, skinNumber)
  }
}
