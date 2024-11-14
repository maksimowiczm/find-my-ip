package com.maksimowiczm.findmyip.data.di

import android.content.Context
import androidx.room.Room
import com.maksimowiczm.findmyip.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database"
    ).build()
}

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun provideAddressEntityDao(database: AppDatabase) = database.addressEntityDao()
}
