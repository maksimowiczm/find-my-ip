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
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.network.AddressStatus as SourceAddressStatus
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val dao: AddressEntityDao
) : AddressRepository {
    override fun observeAddressProviderUrl(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<String> {
        val provider = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> ipv4DataSource.providerURL
            InternetProtocolVersion.IPv6 -> ipv6DataSource.providerURL
        }

        return flowOf(provider)
    }

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
                result.toAddressStatus(internetProtocolVersion)
            }
        }
    }

    private fun SourceAddressStatus.toAddressStatus(
        internetProtocolVersion: InternetProtocolVersion
    ): AddressStatus {
        return when (this) {
            is SourceAddressStatus.Error -> return AddressStatus.Error(this.exception)
            SourceAddressStatus.Loading -> return AddressStatus.Loading
            is SourceAddressStatus.Success -> {
                val (ip, networkType) = this.address

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
