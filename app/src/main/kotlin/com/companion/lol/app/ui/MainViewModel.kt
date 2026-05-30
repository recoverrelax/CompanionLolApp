@file:Suppress("OPT_IN_USAGE")

package com.companion.lol.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.navigation.keys.ChampionListKey
import com.companion.lol.app.navigation.keys.InitialScreenKey
import com.companion.lol.app.navigation.keys.LoginKey
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.util.ChampionColorCache
import com.companion.lol.storage.impl.store.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel
@Inject
constructor(
  savedStateHandle: SavedStateHandle,
  val backStack: BackStack<ScreenKey>,
  private val sessionStore: SessionStore,
  val snackBarManager: SnackBarManager,
) : ViewModel() {
  /**
   * we need this to survive rotation but not process death because the images will be refetched and
   * color can change. Using rememberSaveable in the UI layer would need to save the whole list of
   * colors and would survive process death. There is no need. ViewModel just won't recreate the
   * cache on rotation
   */
  val colorCache = ChampionColorCache(viewModelScope)
  private val backStackSaver = BackStackSaver<ScreenKey>(savedStateHandle)

  init {
    backStackSaver.attackBackStack(backStack = backStack, restore = true)

    viewModelScope.launch {
      sessionStore
        .observeEmailAddress()
        .map { it != null }
        .distinctUntilChanged()
        .collectLatest { isLoggedIn ->
          // at this point the backstack could have been
          // potentially restored from saved state after process death
          val currentHistory = backStack.history
          if (!isLoggedIn) {
            // if we are logged out we rewrite the history.
            // we drop private screens and ensure we don't stay on Initial (placeHolder)
            backStack.setHistory(
              currentHistory
                .dropLastWhile { it.requiresAuth() }
                .filterNot { it is InitialScreenKey }
                .ifEmpty { listOf(LoginKey) }
            )
          } else {
            // if we are logged in and on a public-only stack, go to main content
            if (currentHistory.none { it.requiresAuth() }) {
              backStack.setHistory(ChampionListKey)
            }
          }
        }
    }
  }
}
