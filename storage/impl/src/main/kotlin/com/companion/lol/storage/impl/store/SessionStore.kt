package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.companion.lol.storage.impl.model.ids.SessionId
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.SessionQueries
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Singleton
class SessionStore @Inject constructor(database: LolAppDb, private val dbDispatcher: DbDispatcher) :
  SqldelightStore<SessionQueries>(database.sessionQueries) {

  suspend fun insert(value: SessionTable) =
    withContext(dbDispatcher) { queries.insert(value.copy(id = SessionId)) }

  fun observe(): Flow<SessionTable?> = queries.get().asFlow().mapToOneOrNull(dbDispatcher)

  fun observeEmailAddress(): Flow<String?> =
    queries.emailAddress().asFlow().mapToOneOrNull(dbDispatcher)

  suspend fun updateAutoSync(autoSync: Boolean) =
    withContext(dbDispatcher) {
      queries.transaction {
        val current = queries.get().executeAsOne()
        queries.insert(current.copy(autoSync = autoSync))
      }
    }

  suspend fun delete() = withContext(dbDispatcher) { queries.delete().await() }
}
