package com.lol.app.ui.screens.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() :
  ViewModel() {
  val state: StateFlow<LoginState>
    field = MutableStateFlow(LoginState())

  fun onEmailChanged(newEmail: String) {
    state.update { it.copy(email = newEmail) }
  }
}
