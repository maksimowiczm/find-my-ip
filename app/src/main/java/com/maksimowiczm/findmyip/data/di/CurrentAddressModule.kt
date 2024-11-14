package com.maksimowiczm.findmyip.data.di

import com.maksimowiczm.findmyip.data.network.CurrentAddressDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object CurrentAddressModule {
    @Provides
    @Singleton
    fun providePublicAddressDataSource() = CurrentAddressDataSource(
        networkDispatcher = Dispatchers.IO
    )
}
