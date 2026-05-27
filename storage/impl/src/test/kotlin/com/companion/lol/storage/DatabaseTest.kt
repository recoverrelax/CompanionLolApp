package com.companion.lol.storage

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.companion.lol.storage.impl.di.StorageModule
import com.companion.lol.storage.sqldelight.LolAppDb
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

class DatabaseTest {

  @Test
  fun `test database initialization`() {
    // Use JDBC driver for JVM-based unit tests
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    // create the schema
    LolAppDb.Schema.create(driver)

    // Initialize the database
    val database = StorageModule.database(driver)

    // Basic verification
    assertNotNull(database)
    // Verify we can access queries and the database is empty
    val hasData = database.championQueries.hasData().executeAsOne()
    assertFalse("Database should be empty after initialization", hasData)
  }
}
