package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/** Migration from 4.1.2 to 5.0.0 */
internal object FindMyIP5Migration : Migration(3, 4) {
    override fun migrate(connection: SQLiteConnection) {
        // Create the new AddressHistory table
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS AddressHistory (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                address TEXT NOT NULL,
                domain TEXT,
                addressVersion INTEGER NOT NULL,
                networkType TEXT NOT NULL,
                epochSeconds INTEGER NOT NULL
            )
            """
                .trimIndent()
        )

        // Migrate data from Address to AddressHistory
        // Convert EpochMillis to EpochSeconds by dividing by 1000
        // Convert InternetProtocol: 0 -> 4, 1 -> 6
        // Set domain to NULL for all existing records
        connection.execSQL(
            """
                INSERT INTO AddressHistory (id, address, domain, addressVersion, networkType, epochSeconds)
                SELECT 
                    Id,
                    Ip,
                    NULL AS domain,
                    CASE InternetProtocol 
                        WHEN 0 THEN 4
                        WHEN 1 THEN 6
                        ELSE NULL
                    END AS addressVersion,
                    NetworkType,
                    EpochMillis / 1000 AS epochSeconds
                FROM Address
            """
                .trimIndent()
        )

        // Drop the old Address table
        connection.execSQL("DROP TABLE IF EXISTS Address")
    }
}
