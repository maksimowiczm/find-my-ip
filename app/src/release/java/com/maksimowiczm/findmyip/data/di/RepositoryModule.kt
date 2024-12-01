package com.maksimowiczm.findmyip.data.di

import android.content.Context
import com.maksimowiczm.findmyip.data.database.AppDatabase
import com.maksimowiczm.findmyip.data.database.entity.AddressEntityDao
import com.maksimowiczm.findmyip.data.network.CurrentAddressDataSource
import com.maksimowiczm.findmyip.data.network.IPv4DataSource
import com.maksimowiczm.findmyip.data.network.IPv6DataSource
import com.maksimowiczm.findmyip.data.repository.PublicAddressRepository
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import com.maksimowiczm.findmyip.data.repository.impl.PublicAddressRepositoryImpl
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
    fun providePublicAddressRepository(
        @IPv4DataSource ipv4DataSource: CurrentAddressDataSource,
        @IPv6DataSource ipv6DataSource: CurrentAddressDataSource,
        database: AppDatabase,
        addressEntityDao: AddressEntityDao
    ): PublicAddressRepository = PublicAddressRepositoryImpl(
        ipv4DataSource = ipv4DataSource,
        ipv6DataSource = ipv6DataSource,
        database = database,
        addressEntityDao = addressEntityDao
    )

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository = UserPreferencesRepositoryImpl(
        context = context,
        ioDispatcher = Dispatchers.IO
    )
}
