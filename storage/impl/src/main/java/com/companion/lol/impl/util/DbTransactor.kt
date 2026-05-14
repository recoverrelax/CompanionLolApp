package com.companion.lol.impl.util

interface DbTransactor {
    fun runInTransaction(
        body: () -> Unit,
    )
}