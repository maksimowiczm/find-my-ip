package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.exclusiveTransaction
import androidx.room.immediateTransaction
import androidx.room.migration.Migration
import androidx.room.useWriterConnection
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.transaction.TransactionProvider
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.transaction.TransactionScope
import com.maksimowiczm.findmyip.shared.core.infrastructure.room.AddressHistoryDao
import com.maksimowiczm.findmyip.shared.core.infrastructure.room.AddressHistoryEntity
import com.maksimowiczm.findmyip.shared.core.infrastructure.room.AddressVersionTypeConverter
import com.maksimowiczm.findmyip.shared.core.infrastructure.room.NetworkTypeTypeConverter
import com.maksimowiczm.findmyip.shared.core.infrastructure.room.RoomTransactionScope

@Database(
    entities = [AddressHistoryEntity::class],
    version = FindMyIpDatabase.VERSION,
    autoMigrations =
        [
            AutoMigration(from = 1, to = 2)
            /** @see [MIGRATION_2_3] 3.0.0 -> 4.0.0 migration */
            /** @see [FindMyIP5Migration] 4.1.2 -> 5.0.0 migration */
        ],
    exportSchema = true,
)
@TypeConverters(AddressVersionTypeConverter::class, NetworkTypeTypeConverter::class)
internal abstract class FindMyIpDatabase : RoomDatabase(), TransactionProvider {

    abstract val addressHistoryDao: AddressHistoryDao

    override suspend fun <T> immediate(block: suspend TransactionScope<T>.() -> T): T =
        useWriterConnection {
            it.immediateTransaction {
                val scope = RoomTransactionScope(this)
                block(scope)
            }
        }

    override suspend fun <T> exclusive(block: suspend TransactionScope<T>.() -> T): T =
        useWriterConnection {
            it.exclusiveTransaction {
                val scope = RoomTransactionScope(this)
                block(scope)
            }
        }

    companion object {
        const val VERSION = 4

        private val migrations = arrayOf(MIGRATION_2_3, FindMyIP5Migration)

        fun Builder<FindMyIpDatabase>.buildDatabase(): FindMyIpDatabase {
            addMigrations(*migrations)
            return build()
        }
    }
}

/**
 * This is 3.0.0 -> 4.0.0 migration. Create new Address table with new schema and copy data from old
 * AddressEntity table.
 */
private val MIGRATION_2_3 =
    object : Migration(2, 3) {
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
            """
                    .trimIndent()
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
            """
                    .trimIndent()
            )

            connection.execSQL(
                """
            DROP TABLE AddressEntity
            """
                    .trimIndent()
            )
        }
    }
