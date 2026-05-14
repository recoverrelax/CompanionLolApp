package com.lol.app.ui.screens.login

import android.util.Patterns
import com.lol.app.base.ComposeState

data class LoginState(val email: String = "johnny.crq@gmail.com") : ComposeState {
  val isEmailValid: Boolean = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
