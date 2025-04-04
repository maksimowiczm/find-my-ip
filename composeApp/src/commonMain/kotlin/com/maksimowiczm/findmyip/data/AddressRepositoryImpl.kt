package com.maksimowiczm.findmyip.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.model.NetworkType
import com.maksimowiczm.findmyip.database.AddressDao
import com.maksimowiczm.findmyip.database.AddressEntity
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import com.maksimowiczm.findmyip.domain.TestInternetProtocolsUseCase
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import com.maksimowiczm.findmyip.network.AddressStatus
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.Clock

internal class AddressRepositoryImpl(
    private val ipv4source: NetworkAddressDataSource,
    private val ipv6source: NetworkAddressDataSource,
    private val dataStore: DataStore<Preferences>,
    database: FindMyIpDatabase,
    private val ioApplicationScope: CoroutineScope
) : AddressRepository,
    TestInternetProtocolsUseCase {
    private val addressDao: AddressDao = database.addressDao

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeAddress(internetProtocolVersion: InternetProtocolVersion): Flow<Address> {
        // Check if the user has already tested the protocols before
        ioApplicationScope.launch {
            val tested = dataStore.get(PreferenceKeys.ipFeaturesTested) ?: false
            if (!tested) {
                testInternetProtocols()
            }
        }

        return when (internetProtocolVersion) {
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
    }

    override fun refreshAddresses() {
        ioApplicationScope.launch {
            if (dataStore.get(PreferenceKeys.ipv4Enabled) == true) {
                ipv4source.refreshAddress()
            }

            if (dataStore.get(PreferenceKeys.ipv6Enabled) == true) {
                ipv6source.refreshAddress()
            }
        }
    }

    private suspend fun handleAddressEmission(
        address: Address,
        protocolVersion: InternetProtocolVersion
    ) {
        if (address !is Address.Success) {
            return
        }

        if (!shouldSaveAddress(address)) {
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

    private suspend fun shouldSaveAddress(address: Address.Success): Boolean {
        val save = dataStore.get(PreferenceKeys.historyEnabled) ?: false

        if (!save) {
            return false
        }

        return when (address.networkType) {
            NetworkType.WIFI -> dataStore.get(PreferenceKeys.saveWifiHistory) ?: false
            NetworkType.MOBILE -> dataStore.get(PreferenceKeys.saveMobileHistory) ?: false
            NetworkType.VPN -> dataStore.get(PreferenceKeys.saveVpnHistory) ?: false
            NetworkType.UNKNOWN -> false
        }
    }

    override suspend fun testInternetProtocols() {
        coroutineScope {
            val (ipv4test, ipv6test) = awaitAll(
                async {
                    withTimeout(5_000) {
                        ipv4source.blockingRefreshAddress()
                    }
                },
                async {
                    withTimeout(5_000) {
                        ipv6source.blockingRefreshAddress()
                    }
                }
            )

            // If both test fail set IPv4 as default.
            val ipv4 = if (ipv4test.isFailure && ipv6test.isFailure) {
                true
            } else {
                ipv4test.isSuccess
            }
            val ipv6 = ipv6test.isSuccess

            dataStore.set(
                PreferenceKeys.ipv4Enabled to ipv4,
                PreferenceKeys.ipv6Enabled to ipv6,
                PreferenceKeys.ipFeaturesTested to true
            )
        }
    }
}

private fun AddressStatus.toAddress(): Address = when (this) {
    AddressStatus.None,
    AddressStatus.InProgress -> Address.Loading

    is AddressStatus.Success -> Address.Success(
        ip = address.ip,
        networkType = networkType
    )

    is AddressStatus.Error -> Address.Error(exception.message ?: "Unknown error")
}
