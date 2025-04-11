package com.maksimowiczm.findmyip.infrastructure.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import org.koin.dsl.module

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<FindMyIpDatabase> =
    Room.databaseBuilder(
        context = context,
        name = DATABASE_NAME
    )

actual val databaseModule = module {
    single {
        getRoomDatabase(
            getDatabaseBuilder(get()).setDriver(AndroidSQLiteDriver())
        )
    }
}
