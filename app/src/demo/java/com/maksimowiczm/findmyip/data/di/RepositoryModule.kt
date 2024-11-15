package com.maksimowiczm.findmyip.data.di

import android.content.Context
import com.maksimowiczm.findmyip.data.repository.DemoPublicAddressRepository
import com.maksimowiczm.findmyip.data.repository.PublicAddressRepository
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import com.maksimowiczm.findmyip.data.repository.impl.UserPreferencesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePublicAddressRepository(): PublicAddressRepository = DemoPublicAddressRepository()

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository = UserPreferencesRepositoryImpl(
        context = context,
        ioDispatcher = Dispatchers.IO
    )
}
