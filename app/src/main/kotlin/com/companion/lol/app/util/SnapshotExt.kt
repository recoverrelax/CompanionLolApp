package com.companion.lol.app.util

import androidx.compose.runtime.snapshots.Snapshot

inline fun withSnapshot(action: () -> Unit) {
  Snapshot.withMutableSnapshot { action() }
}
