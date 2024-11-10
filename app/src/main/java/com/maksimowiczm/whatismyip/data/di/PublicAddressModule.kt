package com.maksimowiczm.whatismyip.data.di

import com.maksimowiczm.whatismyip.data.network.PublicAddressDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PublicAddressModule {
    @Provides
    @Singleton
    fun providePublicAddressDataSource() = PublicAddressDataSource(
        networkDispatcher = Dispatchers.IO
    )
}