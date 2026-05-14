@file:Suppress("OPT_IN_USAGE")

package com.lol.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import com.companion.lol.impl.store.PreferencesStore
import com.lol.app.navigation.BackStack
import com.lol.app.navigation.ChampionListKey
import com.lol.app.navigation.InitialScreenKey
import com.lol.app.navigation.LoginKey
import com.lol.app.navigation.ScreenKey
import com.lol.storage.tables.PreferencesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@HiltViewModel
class MainViewModel
@Inject
constructor(private val preferencesStore: PreferencesStore, savedStateHandle: SavedStateHandle) :
  ViewModel() {

  val backStack: BackStack<ScreenKey> by
    savedStateHandle.saved(serializer = HISTORY_SERIALIZER) {
      BackStack.Impl(initialHistory = listOf(InitialScreenKey))
    }

  init {
    if (backStack.history.contains(InitialScreenKey)) {
      viewModelScope.launch {
        val preferences = preferencesStore.get()
        backStack.setHistory(
          if (preferences?.emailAddress == null) {
            LoginKey
          } else {
            ChampionListKey
          }
        )
      }
    }
  }

  fun onLoginClicked(emailAddress: String) {
    viewModelScope.launch {
      preferencesStore.insert(PreferencesEntity(id = 0L, emailAddress = emailAddress))
      backStack.setHistory(ChampionListKey)
    }
  }

  fun onLogoutClicked() {
    viewModelScope.launch {
      preferencesStore.delete()
      backStack.setHistory(LoginKey)
    }
  }
}

private val HISTORY_SERIALIZER =
  object : KSerializer<BackStack<ScreenKey>> {
    private val delegate = ListSerializer(ScreenKey.serializer())

    override val descriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: BackStack<ScreenKey>) {
      encoder.encodeSerializableValue(delegate, value.history.toList())
    }

    override fun deserialize(decoder: Decoder): BackStack<ScreenKey> {
      return BackStack.Impl(initialHistory = decoder.decodeSerializableValue(delegate))
    }
  }
