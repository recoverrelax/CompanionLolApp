package com.companion.lol.impl.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val dbDispatcher = Dispatchers.IO
suspend fun <T> withDbContext(
    block: suspend CoroutineScope.() -> T
): T = withContext(dbDispatcher, block)
