package com.maksimowiczm.findmyip.di

import androidx.room.Room
import com.maksimowiczm.findmyip.infrastructure.room.FindMyIpDatabase
import com.maksimowiczm.findmyip.infrastructure.room.FindMyIpDatabase.Companion.buildDatabase
import org.koin.core.scope.Scope

internal actual fun Scope.database(): FindMyIpDatabase =
    Room.databaseBuilder(
            context = get(),
            klass = FindMyIpDatabase::class.java,
            name = DATABASE_NAME,
        )
        .buildDatabase()
