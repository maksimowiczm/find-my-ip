package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [], version = FindMyIpDatabase.VERSION, exportSchema = false)
abstract class FindMyIpDatabase : RoomDatabase() {

    companion object {
        const val VERSION = 1
    }
}
