@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.infrastructure.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.maksimowiczm.findmyip._2.infrastructure.desktop.preferencesDirectory
import com.maksimowiczm.findmyip.database.AddressEntityDao
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
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
    single {
        getRoomDatabase(
            getDatabaseBuilder().setDriver(BundledSQLiteDriver())
        )
    }

    factory<AddressEntityDao> {
        get<FindMyIpDatabase>().addressEntityDao()
    }
}
