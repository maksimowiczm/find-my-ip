package com.maksimowiczm.findmyip.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.maksimowiczm.findmyip.data.database.entity.AddressEntity
import com.maksimowiczm.findmyip.data.database.entity.AddressEntityDao

@Database(
    entities = [AddressEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun addressEntityDao(): AddressEntityDao
}
