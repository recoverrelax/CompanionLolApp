package com.lol.app.navigation

import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList

interface BackStack<S : ScreenKey> {
  val history: List<S>

  val current: S

  fun goTo(key: S)

  fun setHistory(singleKey: S)

  fun setHistory(newHistory: List<S>)

  fun goBack(): Boolean

  class Impl<S : ScreenKey>(private val initialHistory: List<S>) : BackStack<S> {

    private val _history: SnapshotStateList<S> =
      SnapshotStateList<S>().apply { addAll(initialHistory) }

    override val history: List<S>
      get() = _history

    override val current: S
      get() = _history.last()

    override fun setHistory(singleKey: S) = setHistory(listOf(singleKey))

    override fun setHistory(newHistory: List<S>) {
      Snapshot.withMutableSnapshot {
        _history.clear()
        _history.addAll(newHistory)
      }
    }

    override fun goTo(key: S) {
      Snapshot.withMutableSnapshot {
        if (_history.lastOrNull() == key) return

        val index = _history.indexOf(key)
        if (index != -1) {
          while (_history.size > index + 1) {
            _history.removeAt(_history.size - 1)
          }
        } else {
          _history.add(key)
        }
      }
    }

    override fun goBack(): Boolean = _history.removeLastOrNull() != null
  }
}
