package com.companion.lol.app.util

import androidx.compose.ui.graphics.Color
import com.companion.lol.storage.impl.model.ids.ChampionId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChampionColorCacheTest {
  private val defaultColor = Color.Red
  private val championId = ChampionId(1)
  private val blueColor = Color.Blue
  private val greenColor = Color.Green
  private val fakeInput: String = "fake"

  private fun TestScope.createCache(extractColor: (String) -> Color = { blueColor }) =
    ChampionColorCacheExtractor.Impl(
      scope = backgroundScope,
      extractDispatcher = Dispatchers.Unconfined,
      defaultColor = defaultColor,
      extractColor = extractColor,
    )

  @Test
  fun `getColor returns default color for new champion id`() = runTest {
    val cache = createCache()

    val color = cache.getColor(championId)

    assertEquals(defaultColor, color)
  }

  @Test
  fun `putColor updates the color for champion id`() = runTest {
    val cache = createCache()

    cache.putColor(championId, greenColor)

    assertEquals(greenColor, cache.getColor(championId))
  }

  @Test
  fun `isDefaultColor returns true when color has not been changed`() = runTest {
    val cache = createCache()

    assertTrue(cache.isDefaultColor(championId))
  }

  @Test
  fun `isDefaultColor returns false after putColor with non-default color`() = runTest {
    val cache = createCache()

    cache.putColor(championId, greenColor)

    assertFalse(cache.isDefaultColor(championId))
  }

  @Test
  fun `extractColor updates cache when current color is default`() = runTest {
    val cache = createCache { blueColor }

    cache.extractColor(fakeInput, championId)
    runCurrent()
    assertEquals(blueColor, cache.getColor(championId))
  }

  @Test
  fun `extractColor does nothing when current color is already non-default`() = runTest {
    val cache = createCache { blueColor }
    cache.putColor(championId, greenColor)

    cache.extractColor(fakeInput, championId)

    assertEquals(greenColor, cache.getColor(championId))
  }

  @Test
  fun `extractColor is called only once for consecutive calls`() = runTest {
    var callCount = 0
    val cache = createCache {
      callCount++
      blueColor
    }

    // Both calls happen before the background worker processes the first one
    cache.extractColor(fakeInput, championId)
    cache.extractColor(fakeInput, championId)
    runCurrent()

    assertEquals(1, callCount)
  }
}
