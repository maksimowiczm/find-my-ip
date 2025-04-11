package com.maksimowiczm.findmyip.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.model.NetworkType
import com.maksimowiczm.findmyip.database.AddressDao
import com.maksimowiczm.findmyip.database.AddressEntity
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import com.maksimowiczm.findmyip.domain.ClearHistoryUseCase
import com.maksimowiczm.findmyip.domain.ObserveAddressUseCase
import com.maksimowiczm.findmyip.domain.RefreshAddressesUseCase
import com.maksimowiczm.findmyip.domain.RefreshAndGetIfLatestUseCase
import com.maksimowiczm.findmyip.domain.RefreshAndGetIfLatestUseCase.AddressResult
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

internal class AddressRepository(
    private val ipv4source: NetworkAddressDataSource,
    private val ipv6source: NetworkAddressDataSource,
    private val dataStore: DataStore<Preferences>,
    database: FindMyIpDatabase,
    private val ioApplicationScope: CoroutineScope
) : ObserveAddressUseCase,
    RefreshAddressesUseCase,
    TestInternetProtocolsUseCase,
    RefreshAndGetIfLatestUseCase,
    ClearHistoryUseCase {
    private val addressDao: AddressDao = database.addressDao

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<ObserveAddressUseCase.AddressStatus> {
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
                            ipv4source.addressFlow.map {
                                it.toAddress(InternetProtocolVersion.IPv4)
                            }
                        } else {
                            flowOf(ObserveAddressUseCase.AddressStatus.Disabled)
                        }
                    }

            InternetProtocolVersion.IPv6 ->
                dataStore
                    .observe(PreferenceKeys.ipv6Enabled)
                    .distinctUntilChanged()
                    .flatMapLatest {
                        if (it == true) {
                            ipv6source.refreshAddress()
                            ipv6source.addressFlow.map {
                                it.toAddress(InternetProtocolVersion.IPv6)
                            }
                        } else {
                            flowOf(ObserveAddressUseCase.AddressStatus.Disabled)
                        }
                    }
        }.onEach {
            if (it is ObserveAddressUseCase.AddressStatus.Success) {
                // Don't block
                ioApplicationScope.launch {
                    handleAddressEmission(it.address, internetProtocolVersion)
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

    private suspend fun shouldSaveAddress(address: Address): Boolean {
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

    override suspend fun refreshAndGetIfLatest(protocol: InternetProtocolVersion): AddressResult {
        // Check if protocol is enabled
        val enabled = when (protocol) {
            InternetProtocolVersion.IPv4 -> dataStore.get(PreferenceKeys.ipv4Enabled)
            InternetProtocolVersion.IPv6 -> dataStore.get(PreferenceKeys.ipv6Enabled)
        } ?: false

        if (!enabled) {
            return AddressResult.Disabled
        }

        // Fetch the address
        val addressStatus = when (protocol) {
            InternetProtocolVersion.IPv4 -> ipv4source.blockingRefreshAddress()
            InternetProtocolVersion.IPv6 -> ipv6source.blockingRefreshAddress()
        }.getOrElse {
            return AddressResult.Error(it)
        }

        // Check if the address is already in the database
        val latest = addressDao.getLatest(protocol)

        val address = if (latest != null) {
            if (latest.ip != addressStatus.address) {
                Address(
                    ip = addressStatus.address,
                    networkType = addressStatus.networkType,
                    protocol = protocol
                )
            } else {
                return AddressResult.Duplicate(
                    address = Address(
                        ip = latest.ip,
                        networkType = addressStatus.networkType,
                        protocol = protocol
                    )
                )
            }
        } else {
            Address(
                ip = addressStatus.address,
                networkType = addressStatus.networkType,
                protocol = protocol
            )
        }

        handleAddressEmission(address, protocol)

        return AddressResult.Success(address)
    }

    override suspend fun clearHistory() {
        addressDao.deleteAll()
    }
}

private fun AddressStatus.toAddress(protocolVersion: InternetProtocolVersion) = when (this) {
    AddressStatus.None,
    AddressStatus.InProgress -> ObserveAddressUseCase.AddressStatus.Loading

    is AddressStatus.Success -> ObserveAddressUseCase.AddressStatus.Success(
        Address(
            ip = address,
            networkType = networkType,
            protocol = protocolVersion
        )
    )

    is AddressStatus.Error -> ObserveAddressUseCase.AddressStatus.Error(exception)
}
