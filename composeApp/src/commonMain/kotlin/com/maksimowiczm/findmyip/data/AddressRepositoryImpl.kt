package com.maksimowiczm.findmyip.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.database.AddressDao
import com.maksimowiczm.findmyip.database.AddressEntity
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.network.AddressStatus
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

internal class AddressRepositoryImpl(
    private val ipv4source: NetworkAddressDataSource,
    private val ipv6source: NetworkAddressDataSource,
    private val dataStore: DataStore<Preferences>,
    database: FindMyIpDatabase,
    private val ioApplicationScope: CoroutineScope
) : AddressRepository {
    private val addressDao: AddressDao = database.addressDao

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
                    .onEach {
                        // Don't block
                        ioApplicationScope.launch {
                            handleAddressEmission(it, InternetProtocolVersion.IPv4)
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
                    .onEach {
                        // Don't block
                        ioApplicationScope.launch {
                            handleAddressEmission(it, InternetProtocolVersion.IPv6)
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

    private suspend fun handleAddressEmission(
        address: Address,
        protocolVersion: InternetProtocolVersion
    ) {
        if (address !is Address.Success) {
            return
        }

        val save = dataStore.get(PreferenceKeys.historyEnabled) ?: false

        if (!save) {
            return
        }

        val saveDuplicated =
            dataStore.get(PreferenceKeys.historySaveDuplicates) ?: false

        when (saveDuplicated) {
            true -> addressDao.insertIfOlderThanLatest(
                addressEntity = AddressEntity(
                    ip = address.ip,
                    timestamp = Clock.System.now().toEpochMilliseconds(),
                    internetProtocolVersion = protocolVersion
                ),
                difference = 60 * 1000
            )

            false -> addressDao.insertIfDistinct(
                addressEntity = AddressEntity(
                    ip = address.ip,
                    timestamp = Clock.System.now().toEpochMilliseconds(),
                    internetProtocolVersion = protocolVersion
                )
            )
        }
    }
}

private fun AddressStatus.toAddress(): Address = when (this) {
    AddressStatus.None,
    AddressStatus.InProgress -> Address.Loading

    is AddressStatus.Success -> Address.Success(address.ip)
    is AddressStatus.Error -> Address.Error(exception.message ?: "Unknown error")
}
