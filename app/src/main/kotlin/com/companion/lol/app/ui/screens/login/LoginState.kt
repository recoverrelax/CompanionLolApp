package com.companion.lol.app.ui.screens.login

import android.util.Patterns
import com.companion.lol.app.base.ComposeState
import kotlinx.serialization.Serializable

@Serializable
data class LoginState(val email: String = "johnny.crq@gmail.com") : ComposeState {
  val isEmailValid: Boolean = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
