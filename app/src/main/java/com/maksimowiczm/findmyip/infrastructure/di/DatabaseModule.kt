package com.maksimowiczm.findmyip.infrastructure.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.maksimowiczm.findmyip.data.database.FindMyIPDatabase
import com.maksimowiczm.findmyip.data.database.FindMyIPDatabase.Companion.buildDatabase
import com.maksimowiczm.findmyip.domain.source.AddressLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

const val DATABASE_NAME = "database"

val databaseModule = module {
    single {
        val builder: RoomDatabase.Builder<FindMyIPDatabase> =
            Room.databaseBuilder(
                context = androidContext(),
                klass = FindMyIPDatabase::class.java,
                name = DATABASE_NAME
            )

        builder.buildDatabase()
    }

    factory { database.addressDao }.bind<AddressLocalDataSource>()
}

private val Scope.database
    get() = get<FindMyIPDatabase>()
