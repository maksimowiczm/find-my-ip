package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

actual class FindMyIP5MigrationTest : AbstractFindMyIP5MigrationTest() {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val file = instrumentation.targetContext.getDatabasePath("FindMyIP5MigrationTest.db")
    private val driver: SQLiteDriver = AndroidSQLiteDriver()

    @get:Rule
    val helper: MigrationTestHelper =
        MigrationTestHelper(
            instrumentation = instrumentation,
            file = file,
            driver = driver,
            databaseClass = FindMyIpDatabase::class,
        )

    actual override fun getTestHelper() = helper

    @Test actual override fun migrate() = super.migrate()
}
