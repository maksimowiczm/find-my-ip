package com.maksimowiczm.findmyip.data.di

import android.content.Context
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideUserPreferencesRepository(@ApplicationContext context: Context) =
        UserPreferencesRepository(
            context = context,
            ioDispatcher = Dispatchers.IO
        )
}
