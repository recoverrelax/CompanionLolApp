package com.lol.app.ui.screens

data class RefreshState(
  val refreshing: Boolean = true,
  val initialSync: Boolean = true,
  val isForced: Boolean = false,
  val hasError: Boolean = false,
)
