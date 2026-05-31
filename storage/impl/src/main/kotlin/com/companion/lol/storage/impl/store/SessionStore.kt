package com.companion.lol.storage.impl.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.companion.lol.storage.impl.store.base.SqldelightStore
import com.companion.lol.storage.impl.util.DatabaseContext
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.SessionQueries
import com.companion.lol.storage.sqldelight.tables.SessionTable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Singleton
class SessionStore @Inject constructor(database: LolAppDb, private val context: DatabaseContext) :
  SqldelightStore<SessionQueries>(database.sessionQueries) {

  suspend fun insert(value: SessionTable) = withContext(context) { queries.insert(value) }

  fun observe(): Flow<SessionTable?> = queries.get().asFlow().mapToOneOrNull(context)

  fun observeEmailAddress(): Flow<String?> = queries.emailAddress().asFlow().mapToOneOrNull(context)

  suspend fun updateAutoSync(autoSync: Boolean) =
    withContext(context) { queries.updateAutoSync(autoSync) }

  suspend fun delete() = withContext(context) { queries.delete() }
}
