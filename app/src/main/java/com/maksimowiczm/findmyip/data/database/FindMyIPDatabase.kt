package com.maksimowiczm.findmyip.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.maksimowiczm.findmyip.data.model.AddressEntity

@Database(
    entities = [AddressEntity::class],
    version = FindMyIPDatabase.VERSION,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
        /**
         * @see [MIGRATION_2_3]
         * 3.0.0 -> 4.0.0 migration
         */
    ],
    exportSchema = true
)
@TypeConverters(
    InternetProtocolConverter::class,
    NetworkTypeConverter::class
)
abstract class FindMyIPDatabase : RoomDatabase() {
    abstract val addressDao: AddressDao

    companion object {
        const val VERSION = 3

        private val migrations: List<Migration> = listOf(
            MIGRATION_2_3
        )

        fun Builder<FindMyIPDatabase>.buildDatabase(): FindMyIPDatabase {
            migrations.forEach(::addMigrations)
            return build()
        }
    }
}

/**
 * This is 3.0.0 -> 4.0.0 migration.
 * Create new Address table with new schema and copy data from old AddressEntity table.
 */
private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `Address` (
                `Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `Ip` TEXT NOT NULL,
                `InternetProtocol` INTEGER NOT NULL,
                `NetworkType` TEXT NOT NULL,
                `EpochMillis` INTEGER NOT NULL
            )
            """.trimIndent()
        )

        connection.execSQL(
            """
            INSERT INTO `Address` (`Id`, `Ip`, `InternetProtocol`, `NetworkType`, `EpochMillis`)
            SELECT
              id,
              ip,
              CASE internetProtocolVersion
                WHEN 'IPv4' THEN 0
                WHEN 'IPv6' THEN 1
              END AS InternetProtocol,
              "wifi" AS NetworkType,
              timestamp
            FROM AddressEntity
            """.trimIndent()
        )

        connection.execSQL(
            """
            DROP TABLE AddressEntity
            """.trimIndent()
        )
    }
}
