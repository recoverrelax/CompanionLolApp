package com.companion.lol.app.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.companion.lol.app.navigation.keys.ScreenKey
import com.companion.lol.app.util.withSnapshot

@Stable
interface BackStack<S : ScreenKey> {

  val history: List<S>

  val current: S

  fun goTo(key: S)

  fun setHistory(singleKey: S)

  fun setHistory(newHistory: List<S>)

  fun goBack(): Boolean

  class Impl<S : ScreenKey>(private val initialValue: List<S>) : BackStack<S> {
    var saver: ((List<S>) -> Unit) = {}
    override val history = SnapshotStateList<S>().apply { addAll(initialValue) }

    override val current: S
      get() = history.last()

    override fun setHistory(singleKey: S) =
      withSnapshot {
          history.clear()
          history.add(singleKey)
        }
        .also { save() }

    override fun setHistory(newHistory: List<S>) =
      withSnapshot {
          history.clear()
          history.addAll(newHistory)
        }
        .also { save() }

    override fun goTo(key: S) =
      withSnapshot {
          val last =
            history.lastOrNull()
              ?: error("Cannot use goTo without having a valid non-empty history")

          // no repeated keys allowed
          if (last == key) return@withSnapshot

          val index = history.indexOf(key)
          if (index != -1) {
            while (history.size > index + 1) {
              history.removeAt(history.size - 1)
            }
          } else {
            history.add(key)
          }
        }
        .also { save() }

    override fun goBack(): Boolean {
      if (history.size > 1) {
        history.removeAt(history.size - 1)
        save()
        return true
      }
      return false
    }

    private fun save() {
      saver(this.history.toList())
    }
  }
}
