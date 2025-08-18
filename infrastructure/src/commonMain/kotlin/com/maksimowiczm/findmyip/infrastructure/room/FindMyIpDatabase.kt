package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [AddressHistoryEntity::class],
    version = FindMyIpDatabase.VERSION,
    exportSchema = false,
)
@TypeConverters(AddressVersionTypeConverter::class)
internal abstract class FindMyIpDatabase : RoomDatabase() {

    abstract val addressHistoryDao: AddressHistoryDao

    companion object {
        const val VERSION = 1

        fun Builder<FindMyIpDatabase>.buildDatabase(): FindMyIpDatabase {
            return build()
        }
    }
}
