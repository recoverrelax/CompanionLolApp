package com.lol.app.navigation

import kotlinx.serialization.Serializable

@Serializable sealed interface ScreenKey

@Serializable data object InitialScreenKey : ScreenKey

@Serializable data object LoginKey : ScreenKey

@Serializable data object ChampionListKey : ScreenKey, MainBottomNavScreen

@Serializable data object SettingsKey : ScreenKey, MainBottomNavScreen
