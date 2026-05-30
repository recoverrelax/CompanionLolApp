@file:Suppress("ClassName")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.companion.lol.app.navigation.keys

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.EntryProviderScope
import com.companion.lol.app.navigation.ScreenMetadata
import kotlin.reflect.KClass
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed interface ScreenKey {
  val metadata: Map<String, Any>
    get() = emptyMap()

  fun requiresAuth(): Boolean {
    return false
  }

  @Composable fun Content()

  companion object {
    @Stable inline fun <reified S : ScreenKey> id(): String = id(S::class)

    @Stable fun <S : ScreenKey> id(instance: KClass<S>): String = instance.simpleName!!
  }
}

inline fun <reified K : ScreenKey> EntryProviderScope<ScreenKey>.entryScreenKey() {
  entry(
    metadata = { key -> ScreenMetadata.screenKey(key) + key.metadata },
    content = { key: K -> key.Content() },
  )
}
