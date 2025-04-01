package com.maksimowiczm.findmyip.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.network.AddressStatus
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class AddressRepositoryImpl(
    private val ipv4source: NetworkAddressDataSource,
    private val ipv6source: NetworkAddressDataSource,
    private val dataStore: DataStore<Preferences>
) : AddressRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeAddress(internetProtocolVersion: InternetProtocolVersion): Flow<Address> =
        when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 ->
                dataStore
                    .observe(PreferenceKeys.ipv4Enabled)
                    .distinctUntilChanged()
                    .flatMapLatest {
                        if (it == true) {
                            ipv4source.refreshAddress()
                            ipv4source.addressFlow.map { it.toAddress() }
                        } else {
                            flowOf(Address.Disabled)
                        }
                    }

            InternetProtocolVersion.IPv6 ->
                dataStore
                    .observe(PreferenceKeys.ipv6Enabled)
                    .distinctUntilChanged()
                    .flatMapLatest {
                        if (it == true) {
                            ipv6source.refreshAddress()
                            ipv6source.addressFlow.map { it.toAddress() }
                        } else {
                            flowOf(Address.Disabled)
                        }
                    }
        }

    override suspend fun refreshAddresses() {
        if (dataStore.get(PreferenceKeys.ipv4Enabled) == true) {
            ipv4source.refreshAddress()
        }

        if (dataStore.get(PreferenceKeys.ipv6Enabled) == true) {
            ipv6source.refreshAddress()
        }
    }
}

private fun AddressStatus.toAddress(): Address = when (this) {
    AddressStatus.None,
    AddressStatus.InProgress -> Address.Loading

    is AddressStatus.Success -> Address.Success(address.ip)
    is AddressStatus.Error -> Address.Error(exception.message ?: "Unknown error")
}
