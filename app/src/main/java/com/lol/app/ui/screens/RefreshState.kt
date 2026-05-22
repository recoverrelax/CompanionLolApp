package com.lol.app.ui.screens

data class RefreshState(
  val isRefreshing: Boolean = true,
  val isInitialSync: Boolean = true,
  val isForced: Boolean = false,
  val withError: Boolean = false,
)
