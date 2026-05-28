@file:Suppress("ClassName")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.app.navigation.keys

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.get
import com.companion.lol.app.navigation.BackStack
import com.companion.lol.app.navigation.ScreenMetadata
import com.companion.lol.app.ui.LocalBackStack
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed interface ScreenKey {
  val metadata: Map<String, Any>
    get() = emptyMap()

  fun requiresAuth(): Boolean {
    return false
  }

  fun isNavBarEntry(): Boolean {
    return (metadata[ScreenMetadata.TopLevelDestination] ?: false)
  }

  @Composable fun Content(backStack: BackStack<ScreenKey>)
}

inline fun <reified K : ScreenKey> EntryProviderScope<ScreenKey>.entryScreenKey() {
  entry(
    metadata = { key -> ScreenMetadata.screenKey(key) + key.metadata },
    content = { key: K -> key.Content(LocalBackStack.current) },
  )
}
