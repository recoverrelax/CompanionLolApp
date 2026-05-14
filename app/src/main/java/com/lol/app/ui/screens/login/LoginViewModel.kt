package com.lol.app.ui.screens.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
  val state: StateFlow<LoginState>
    field = MutableStateFlow(LoginState())

  fun onEmailChanged(newEmail: String) {
    state.update { it.copy(email = newEmail) }
  }
}
