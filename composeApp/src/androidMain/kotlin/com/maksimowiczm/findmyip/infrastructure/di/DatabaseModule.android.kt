package com.maksimowiczm.findmyip.infrastructure.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.maksimowiczm.findmyip.database.AddressEntityDao
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<FindMyIpDatabase> =
    Room.databaseBuilder(
        context = context,
        name = "database"
    )

actual val databaseModule: Module = module {
    single {
        getRoomDatabase(
            getDatabaseBuilder(get()).setDriver(AndroidSQLiteDriver())
        )
    }

    factory<AddressEntityDao> {
        get<FindMyIpDatabase>().addressEntityDao()
    }
}
