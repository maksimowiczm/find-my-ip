package com.maksimowiczm.findmyip.data.di

import android.content.Context
import com.maksimowiczm.findmyip.BuildConfig
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.network.ConnectivityObserver
import com.maksimowiczm.findmyip.data.network.CurrentAddressDataSource
import com.maksimowiczm.findmyip.data.network.IPv4DataSource
import com.maksimowiczm.findmyip.data.network.IPv6DataSource
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
    @IPv4DataSource
    fun provideIPv4DataSource(connectivityObserver: ConnectivityObserver) =
        CurrentAddressDataSource(
            networkDispatcher = Dispatchers.IO,
            connectivityObserver = connectivityObserver,
            addressProviderUrl = BuildConfig.IPV4_PROVIDER,
            internetProtocolVersion = InternetProtocolVersion.IPv4
        )

    @Provides
    @Singleton
    @IPv6DataSource
    fun provideIPv6DataSource(connectivityObserver: ConnectivityObserver) =
        CurrentAddressDataSource(
            networkDispatcher = Dispatchers.IO,
            connectivityObserver = connectivityObserver,
            addressProviderUrl = BuildConfig.IPV6_PROVIDER,
            internetProtocolVersion = InternetProtocolVersion.IPv6
        )
}
