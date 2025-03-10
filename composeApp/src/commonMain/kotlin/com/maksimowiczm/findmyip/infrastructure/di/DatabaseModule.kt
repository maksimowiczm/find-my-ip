package com.maksimowiczm.findmyip.infrastructure.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import org.koin.core.module.Module

fun getRoomDatabase(builder: RoomDatabase.Builder<FindMyIpDatabase>) = builder
    .setDriver(BundledSQLiteDriver())
    .build()

expect val databaseModule: Module
