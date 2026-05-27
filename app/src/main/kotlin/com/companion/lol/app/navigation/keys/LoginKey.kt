package com.companion.lol.app.navigation.keys

import androidx.compose.runtime.Composable
import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.ui.screens.login.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
data object LoginKey : ScreenKey {
  @Composable
  override fun Content(backStack: BackStack<ScreenKey>) {
    LoginScreen()
  }
}
