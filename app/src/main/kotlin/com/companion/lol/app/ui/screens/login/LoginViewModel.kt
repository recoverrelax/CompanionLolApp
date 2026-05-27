package com.companion.lol.app.ui.screens.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companion.lol.app.util.persistedFlow
import com.companion.lol.storage.impl.model.ids.SessionId
import com.companion.lol.storage.impl.store.SessionStore
import com.companion.lol.storage.sqldelight.tables.SessionTable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel
@Inject
constructor(private val sessionStore: SessionStore, savedStateHandle: SavedStateHandle) :
  ViewModel() {
  val state: StateFlow<LoginState>
    field = savedStateHandle.persistedFlow(key = "LoginState", LoginState())

  fun onEmailChanged(newEmail: String) {
    state.update { it.copy(email = newEmail) }
  }

  fun onLoginClicked() {
    val currentState = this.state.value
    // fail-safe
    if (!currentState.isEmailValid) return

    viewModelScope.launch {
      sessionStore.insert(
        value = SessionTable(id = SessionId, emailAddress = currentState.email, autoSync = false)
      )
    }
  }
}
