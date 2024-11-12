package com.maksimowiczm.whatismyip.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maksimowiczm.whatismyip.data.database.entity.AddressEntity
import com.maksimowiczm.whatismyip.data.database.entity.AddressEntityDao

@Database(
    entities = [AddressEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun addressEntityDao(): AddressEntityDao
}
