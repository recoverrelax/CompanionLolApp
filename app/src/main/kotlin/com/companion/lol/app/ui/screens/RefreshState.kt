package com.companion.lol.app.ui.screens

data class RefreshState(
  val refreshing: Boolean = true,
  val userTriggered: Boolean = false,
  val hasError: Boolean = false,
)
