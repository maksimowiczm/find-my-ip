package com.maksimowiczm.findmyip.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.model.toDomain
import com.maksimowiczm.findmyip.database.AddressEntityDao
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AddressRepositoryImpl(
    private val ipv4DataSource: NetworkAddressDataSource,
    private val ipv6DataSource: NetworkAddressDataSource,
    private val dataStore: DataStore<Preferences>,
    private val dao: AddressEntityDao,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AddressRepository {
    private val ioScope = CoroutineScope(ioDispatcher + SupervisorJob())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<AddressStatus> {
        val preferenceKey = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> PreferenceKeys.ipv4Enabled
            InternetProtocolVersion.IPv6 -> PreferenceKeys.ipv6Enabled
        }

        return dataStore.observe(preferenceKey).flatMapLatest { ipvEnabled ->
            if (ipvEnabled != true) {
                return@flatMapLatest flowOf(AddressStatus.Disabled)
            }

            val flow = when (internetProtocolVersion) {
                InternetProtocolVersion.IPv4 -> ipv4DataSource.observeAddress()
                InternetProtocolVersion.IPv6 -> ipv6DataSource.observeAddress()
            }

            flow.map { result ->
                if (result.isFailure) {
                    return@map AddressStatus.Error(result.exceptionOrNull())
                }

                val (ip, networkType) = result.getOrNull() ?: return@map AddressStatus.Loading

                val address = Address(
                    ip = ip,
                    protocolVersion = internetProtocolVersion,
                    date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    networkType = networkType
                )

                AddressStatus.Success(address)
            }
        }
    }

    override suspend fun refreshAddresses() {
        coroutineScope {
            launch { ipv4DataSource.refreshAddress() }
            launch { ipv6DataSource.refreshAddress() }
        }
    }

    override suspend fun refreshAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): AddressStatus {
        val preferenceKey = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> PreferenceKeys.ipv4Enabled
            InternetProtocolVersion.IPv6 -> PreferenceKeys.ipv6Enabled
        }

        val enabled = dataStore.get(preferenceKey) ?: false

        if (!enabled) {
            return AddressStatus.Disabled
        }

        val source = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> ipv4DataSource
            InternetProtocolVersion.IPv6 -> ipv6DataSource
        }

        val address = source.refreshAddress().map {
            Address(
                ip = it.ip,
                protocolVersion = internetProtocolVersion,
                date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                networkType = it.networkType
            )
        }.getOrElse { exception ->
            return AddressStatus.Error(exception)
        }

        return AddressStatus.Success(address)
    }

    override fun observeAddressesPaged(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<PagingData<Address>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = 30
            )
        ) {
            dao.observeAddressesPaged(internetProtocolVersion = internetProtocolVersion)
        }

        return pager.flow.map { data ->
            data.map { it.toDomain() }
        }
    }

    override suspend fun clearHistory() {
        dao.deleteAll()
    }
}
