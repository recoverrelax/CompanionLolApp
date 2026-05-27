package com.companion.lol.app

import androidx.lifecycle.SavedStateHandle
import com.companion.lol.app.navigation.BackStack.Companion.backStack
import com.companion.lol.app.navigation.keys.ChampionDetailsKey
import com.companion.lol.app.navigation.keys.InitialScreenKey
import com.companion.lol.app.navigation.keys.LoginKey
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.navigation.keys.SettingsKey
import com.companion.lol.storage.impl.model.ids.ChampionId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackStackTest {
  private val screenA = InitialScreenKey
  private val screenB = LoginKey
  private val screenC = SettingsKey
  private val bottomSheetScreen = ChampionDetailsKey(ChampionId(1))

  private fun createBackStack(
    savedStateHandle: SavedStateHandle = SavedStateHandle(),
    initialHistory: List<ScreenKey>,
  ) = savedStateHandle.backStack(initialHistory = initialHistory)

  @Test
  fun `initial history is set correctly`() {
    val backStack = createBackStack(initialHistory = listOf(screenA))

    assertEquals(listOf(screenA), backStack.history)
    assertEquals(screenA, backStack.current)
  }

  @Test
  fun `setHistory single key clears and sets new key`() {
    val backStack = createBackStack(initialHistory = listOf(screenA))

    backStack.setHistory(screenB)

    assertEquals(listOf(screenB), backStack.history)
  }

  @Test
  fun `setHistory multiple keys clears and sets new history`() {
    val backStack = createBackStack(initialHistory = listOf(screenA))

    backStack.setHistory(listOf(screenB, screenC))

    assertEquals(listOf(screenB, screenC), backStack.history)
  }

  @Test
  fun `goTo adds new key to history`() {
    val backStack = createBackStack(initialHistory = listOf(screenA))

    backStack.goTo(screenB)

    assertEquals(listOf(screenA, screenB), backStack.history)
  }

  @Test
  fun `goTo same key type as current is no-op`() {
    val key = bottomSheetScreen
    val backStack = createBackStack(initialHistory = listOf(key))

    val goToKey = key.copy(championId = key.championId.copy(value = key.championId.value + 1))
    backStack.goTo(goToKey)

    assertEquals(listOf(bottomSheetScreen), backStack.history)
  }

  @Test
  fun `goTo existing key in history pops until that key`() {
    val backStack = createBackStack(initialHistory = listOf(screenA, screenB, screenC))

    backStack.goTo(screenA)

    assertEquals(listOf(screenA), backStack.history)
  }

  @Test
  fun `goTo from BottomSheet is no-op`() {
    val backStack = createBackStack(initialHistory = listOf(screenA, bottomSheetScreen))

    backStack.goTo(screenB)
    assertEquals(listOf(screenA, bottomSheetScreen), backStack.history)
  }

  @Test
  fun `goBack removes last item and returns true`() {
    val backStack = createBackStack(initialHistory = listOf(screenA, screenB))

    val result = backStack.goBack()

    assertTrue(result)
    assertEquals(listOf(screenA), backStack.history)
  }

  @Test
  fun `goBack on single item returns false and does nothing`() {
    val backStack = createBackStack(initialHistory = listOf(screenA))

    val result = backStack.goBack()

    assertFalse(result)
    assertEquals(listOf(screenA), backStack.history)
  }

  @Test
  fun `SavedStateHandle backStack extension initializes correctly`() {
    val backStack = createBackStack(initialHistory = listOf(screenA))
    assertEquals(listOf(screenA), backStack.history)
  }
}
