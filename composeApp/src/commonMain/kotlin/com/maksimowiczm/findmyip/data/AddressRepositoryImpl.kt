package com.maksimowiczm.findmyip.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class AddressRepositoryImpl(
    private val ipv4DataSource: NetworkAddressDataSource,
    private val ipv6DataSource: NetworkAddressDataSource,
    private val dataStore: DataStore<Preferences>
) : AddressRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<AddressStatus> {
        val preferenceKey = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> PreferenceKeys.ipv4Enabled
            InternetProtocolVersion.IPv6 -> PreferenceKeys.ipv6Enabled
        }

        return dataStore.observe(preferenceKey).flatMapLatest {
            if (it != true) {
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

                val address = result.getOrNull()

                if (address != null) {
                    AddressStatus.Success(
                        Address(
                            ip = address,
                            protocolVersion = internetProtocolVersion
                        )
                    )
                } else {
                    AddressStatus.Loading
                }
            }
        }
    }

    override fun refreshAddresses() {
        ipv4DataSource.refreshAddress()
        ipv6DataSource.refreshAddress()
    }
}
