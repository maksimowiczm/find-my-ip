package com.maksimowiczm.findmyip.data.di

import android.content.Context
import com.maksimowiczm.findmyip.data.network.ConnectivityObserver
import com.maksimowiczm.findmyip.data.network.CurrentAddressDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object CurrentAddressModule {
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        ConnectivityObserver(context)

    @Provides
    @Singleton
    fun providePublicAddressDataSource(connectivityObserver: ConnectivityObserver) =
        CurrentAddressDataSource(
            networkDispatcher = Dispatchers.IO,
            connectivityObserver = connectivityObserver
        )
}
