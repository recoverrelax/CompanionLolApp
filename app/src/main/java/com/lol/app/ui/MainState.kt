package com.lol.app.ui

import com.lol.app.base.ComposeState
import com.lol.app.navigation.InitialScreenKey
import com.lol.app.navigation.ScreenKey

data class MainState(val navHistory: List<ScreenKey> = listOf(InitialScreenKey)) : ComposeState
