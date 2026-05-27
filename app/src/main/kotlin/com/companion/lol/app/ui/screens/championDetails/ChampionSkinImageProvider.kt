package com.companion.lol.app.ui.screens.championDetails

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import com.companion.lol.data.io.images.DdragonImage
import com.companion.lol.data.model.other.ChampionSkin
import com.companion.lol.storage.impl.model.ids.ChampionId

@Stable
interface ChampionSkinProvider {
  val image: DdragonImage.Skin?

  fun toggleSkin()
}

@VisibleForTesting internal fun ChampionSkinProvider() = Impl()

@VisibleForTesting
internal class Impl : ChampionSkinProvider {
  private var currentIndex by mutableIntStateOf(0)
  private val skins = mutableStateOf<List<DdragonImage.Skin>>(emptyList())
  override val image: DdragonImage.Skin?
    get() = skins.value.getOrNull(currentIndex)

  override fun toggleSkin() {
    currentIndex = nextIndex()
  }

  private fun nextIndex(): Int {
    return if (currentIndex == skins.value.lastIndex) {
      0
    } else {
      currentIndex + 1
    }
  }

  fun updateSkins(skins: List<DdragonImage.Skin>) {
    Snapshot.withMutableSnapshot {
      this.skins.value = skins
      // realign indexes
      currentIndex = if (skins.isEmpty()) 0 else currentIndex.coerceAtMost(skins.lastIndex)
    }
  }
}

@Composable
fun rememberChampionSkinProvider(
  championId: ChampionId,
  skins: List<ChampionSkin>?,
): ChampionSkinProvider {
  return remember(championId) { ChampionSkinProvider() }
    .apply { updateSkins(skins?.map { it.image } ?: emptyList()) }
}
