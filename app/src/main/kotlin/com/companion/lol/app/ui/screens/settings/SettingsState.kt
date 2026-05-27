package com.companion.lol.app.ui.screens.settings

import com.companion.lol.app.base.ComposeState

data class SettingsState(val emailAddress: String, val autoSync: Boolean) : ComposeState
