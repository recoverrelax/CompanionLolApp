package com.companion.lol.storage.impl.di

import android.content.Context
import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.companion.lol.storage.impl.adapter.ChampionIdAdapter
import com.companion.lol.storage.impl.adapter.ChampionTagAdapter
import com.companion.lol.storage.impl.adapter.PartyTypeIdAdapter
import com.companion.lol.storage.impl.adapter.SessionIdAdapter
import com.companion.lol.storage.impl.adapter.SettingsIdAdapter
import com.companion.lol.storage.impl.adapter.SkinIdAdapter
import com.companion.lol.storage.impl.model.other.GridSize
import com.companion.lol.storage.impl.model.other.PartyType
import com.companion.lol.storage.impl.model.other.SortOrder
import com.companion.lol.storage.impl.util.DbDispatcher
import com.companion.lol.storage.impl.util.DbTransacter
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsTable
import com.companion.lol.storage.sqldelight.tables.ChampionFavoritesTable
import com.companion.lol.storage.sqldelight.tables.ChampionPartyTypeTable
import com.companion.lol.storage.sqldelight.tables.ChampionTable
import com.companion.lol.storage.sqldelight.tables.SessionTable
import com.companion.lol.storage.sqldelight.tables.SettingsTable
import com.companion.lol.storage.sqldelight.tables.SkinTable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable

@Module
@InstallIn(SingletonComponent::class)
internal object StorageModule {

  @Provides
  @Singleton
  fun driver(@ApplicationContext context: Context): SqlDriver =
    AndroidSqliteDriver(LolAppDb.Schema, context, "com.lol.storage.db")

  @Provides
  @Singleton
  internal fun transacter(app: LolAppDb): DbTransacter = object : DbTransacter, Transacter by app {}

  @Provides
  @Singleton
  internal fun dispatcher(): DbDispatcher =
    object : DbDispatcher() {
      private val dispatcher = Dispatchers.IO

      override fun dispatch(context: CoroutineContext, block: Runnable) =
        dispatcher.dispatch(context, block)
    }

  @Provides
  @Singleton
  fun database(driver: SqlDriver): LolAppDb =
    LolAppDb(
      driver = driver,
      ChampionTableAdapter = ChampionTable.Adapter(ChampionIdAdapter, PartyTypeIdAdapter),
      ChampionFavoritesTableAdapter = ChampionFavoritesTable.Adapter(ChampionIdAdapter),
      ChampionPartyTypeTableAdapter =
        ChampionPartyTypeTable.Adapter(PartyTypeIdAdapter, EnumColumnAdapter<PartyType>()),
      SettingsTableAdapter =
        SettingsTable.Adapter(
          SettingsIdAdapter,
          EnumColumnAdapter<GridSize>(),
          EnumColumnAdapter<SortOrder>(),
        ),
      SessionTableAdapter = SessionTable.Adapter(SessionIdAdapter),
      SkinTableAdapter = SkinTable.Adapter(ChampionIdAdapter, SkinIdAdapter, IntColumnAdapter),
      ChampionDetailsTableAdapter =
        ChampionDetailsTable.Adapter(
          idAdapter = ChampionIdAdapter,
          tagsAdapter = ChampionTagAdapter,
          partyTypeIdAdapter = PartyTypeIdAdapter,
        ),
    )
}
