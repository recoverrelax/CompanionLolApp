package com.lol.app.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.lol.app.base.theme.Gold1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

val LocalChampionColorCache =
  compositionLocalOf<ChampionColorCache> { error("Not initialized yet") }

@Stable
interface ChampionColorCache {

  val defaultColor: Color

  fun getColor(id: ChampionId): Color

  fun isDefaultColor(id: ChampionId): Boolean

  fun putColor(id: ChampionId, color: Color)

  fun extractColor(input: coil3.Bitmap, championId: ChampionId)

  class Impl(scope: CoroutineScope, override val defaultColor: Color = Gold1) : ChampionColorCache {
    private val cache = hashMapOf<ChampionId, MutableState<Color>>()
    private val extractChannel =
      Channel<Pair<ChampionId, coil3.Bitmap>>(capacity = Channel.UNLIMITED)

    init {
      scope.launch(Dispatchers.IO) {
        for ((championId, input) in extractChannel) {
          Palette.from(input).generate().let {
            putColor(
              id = championId,
              color =
                Color(
                  it.getVibrantColor(0).takeIf { color -> color != 0 } ?: it.getDominantColor(0)
                ),
            )
          }
        }
      }
    }

    @Stable
    private fun getColorState(id: ChampionId): MutableState<Color> =
      cache.getOrPut(id) { mutableStateOf(defaultColor) }

    @Stable
    override fun getColor(id: ChampionId): Color {
      return getColorState(id).value
    }

    override fun putColor(id: ChampionId, color: Color) {
      getColorState(id).value = color
    }

    override fun isDefaultColor(id: ChampionId): Boolean = getColor(id) == defaultColor

    override fun extractColor(input: coil3.Bitmap, championId: ChampionId) {
      if (!isDefaultColor(championId)) return

      extractChannel.trySend(championId to input)
    }
  }
}
