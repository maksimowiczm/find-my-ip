package com.maksimowiczm.findmyip.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.maksimowiczm.findmyip.data.model.AddressEntity

@Database(
    entities = [AddressEntity::class],
    version = FindMyIPDatabase.VERSION,
    exportSchema = true
)
@TypeConverters(
    InternetProtocolConverter::class,
    NetworkTypeConverter::class
)
abstract class FindMyIPDatabase : RoomDatabase() {
    abstract val addressDao: AddressDao

    companion object {
        const val VERSION = 1

        private val migrations: List<Migration> = listOf()

        fun Builder<FindMyIPDatabase>.buildDatabase(): FindMyIPDatabase {
            migrations.forEach(::addMigrations)
            return build()
        }
    }
}
