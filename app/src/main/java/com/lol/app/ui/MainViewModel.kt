@file:Suppress("OPT_IN_USAGE")

package com.lol.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.data.usecase.SessionUseCase
import com.companion.lol.storage.impl.model.ids.ChampionId
import com.lol.app.base.theme.Gold1
import com.lol.app.navigation.BackStack
import com.lol.app.navigation.ChampionDetailsKey
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.InitialScreenKey
import com.lol.app.navigation.LoginKey
import com.lol.app.navigation.ScreenKey
import com.lol.app.util.ChampionColorCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel
@Inject
constructor(private val sessionUseCase: SessionUseCase, savedStateHandle: SavedStateHandle) :
  ViewModel() {

  val backStack: BackStack<ScreenKey> =
    BackStack.Impl(savedStateHandle = savedStateHandle, initialHistory = listOf(InitialScreenKey))
  val colorCache: ChampionColorCache =
    ChampionColorCache.Impl(scope = viewModelScope, defaultColor = Gold1)

  init {
    if (backStack.history.contains(InitialScreenKey)) {
      viewModelScope.launch {
        val emailAddress = sessionUseCase.getEmailAddress()
        backStack.setHistory(
          if (emailAddress == null) {
            LoginKey
          } else {
            ChampionListKey
          }
        )
      }
    }
  }

  fun goToChampionDetails(championId: ChampionId) {
    backStack.goTo(ChampionDetailsKey(championId))
  }

  fun onLoginClicked(emailAddress: String) {
    viewModelScope.launch {
      sessionUseCase.updateEmailAddress(emailAddress)
      backStack.setHistory(ChampionListKey)
    }
  }

  fun onLogoutClicked() {
    viewModelScope.launch {
      sessionUseCase.clear()
      backStack.setHistory(LoginKey)
    }
  }
}
