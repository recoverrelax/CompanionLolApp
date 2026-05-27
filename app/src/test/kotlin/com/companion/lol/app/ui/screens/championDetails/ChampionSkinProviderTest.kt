package com.companion.lol.app.ui.screens.championDetails

import com.companion.lol.data.io.images.DdragonImage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ChampionSkinProviderTest {

  private val createSkin = { num: Int ->
    DdragonImage.Skin(skinNumber = num, skinName = "Skin $num", keyName = "Champion")
  }

  @Test
  fun `initial state with no skins returns null image`() {
    val provider = ChampionSkinProvider()
    assertNull(provider.image)
  }

  @Test
  fun `initial state with skins returns first skin`() {
    val provider = ChampionSkinProvider()
    val skins = listOf(createSkin(0), createSkin(1))
    provider.updateSkins(skins)

    assertEquals(skins[0], provider.image)
  }

  @Test
  fun `toggleSkin cycles through skins`() {
    val provider = ChampionSkinProvider()
    val skins = listOf(createSkin(0), createSkin(1), createSkin(2))
    provider.updateSkins(skins)

    assertEquals(skins[0], provider.image)

    provider.toggleSkin()
    assertEquals(skins[1], provider.image)

    provider.toggleSkin()
    assertEquals(skins[2], provider.image)
  }

  @Test
  fun `toggleSkin wraps around to the first skin`() {
    val provider = ChampionSkinProvider()
    val skins = listOf(createSkin(0), createSkin(1))
    provider.updateSkins(skins)

    provider.toggleSkin() // to index 1
    assertEquals(skins[1], provider.image)

    provider.toggleSkin() // wrap to index 0
    assertEquals(skins[0], provider.image)
  }

  @Test
  fun `toggleSkin does nothing when skins are empty`() {
    val provider = ChampionSkinProvider()
    provider.toggleSkin()
    assertNull(provider.image)
  }

  @Test
  fun `image returns first skin if currentIndex becomes out of bounds due to skins shrinking`() {
    val provider = ChampionSkinProvider()
    val skins = listOf(createSkin(0), createSkin(1))
    provider.updateSkins(skins)
    provider.toggleSkin() // index 1
    assertEquals(1, skins.lastIndex)

    provider.updateSkins(listOf(createSkin(0))) // shrink to 1 item
    assertEquals(createSkin(0), provider.image)
  }

  @Test
  fun `toggleSkin should recover from out of bounds index`() {
    val provider = ChampionSkinProvider()
    provider.updateSkins(listOf(createSkin(0), createSkin(1)))
    provider.toggleSkin() // index 1

    provider.updateSkins(listOf(createSkin(0))) // shrink, index 1 is now out of bounds

    provider.toggleSkin() // return to 0

    assertEquals(createSkin(0), provider.image)
  }
}
