package com.maksimowiczm.findmyip.infrastructure.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.maksimowiczm.findmyip.database.AddressEntityDao
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import com.maksimowiczm.findmyip.infrastructure.desktop.preferencesDirectory
import java.io.File
import org.koin.core.module.Module
import org.koin.dsl.module

fun getDatabaseBuilder(): RoomDatabase.Builder<FindMyIpDatabase> {
    val dbFile = File(preferencesDirectory, "my_room.db")

    return Room.databaseBuilder<FindMyIpDatabase>(
        name = dbFile.absolutePath
    )
}

actual val databaseModule: Module = module {
    single { getRoomDatabase(getDatabaseBuilder()) }

    factory<AddressEntityDao> {
        get<FindMyIpDatabase>().addressEntityDao()
    }
}
