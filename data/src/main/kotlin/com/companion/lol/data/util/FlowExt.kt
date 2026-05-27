package com.companion.lol.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <T, R> Flow<List<T>>.listMap(
  crossinline transform: suspend (value: T) -> R
): Flow<List<R>> {
  return this.map { it.map { element -> transform(element) } }
}
