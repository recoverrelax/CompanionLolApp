package com.companion.lol.app.navigation.keys

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.companion.lol.app.navigation.BackStack
import kotlinx.serialization.Serializable

@Serializable
data object InitialScreenKey : ScreenKey {
  @Composable
  override fun Content(backStack: BackStack<ScreenKey>) {
    Box(modifier = Modifier)
  }
}
