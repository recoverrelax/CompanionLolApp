package com.lol.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

  init {
    Timber.i("Settings init")
  }
}
