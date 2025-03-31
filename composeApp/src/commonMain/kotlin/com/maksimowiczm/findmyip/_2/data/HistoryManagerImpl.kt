@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import co.touchlab.kermit.Logger
import com.maksimowiczm.findmyip._2.data.model.Address
import com.maksimowiczm.findmyip._2.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip._2.data.model.NetworkType
import com.maksimowiczm.findmyip._2.data.model.toEntity
import com.maksimowiczm.findmyip._2.infrastructure.di.get
import com.maksimowiczm.findmyip._2.infrastructure.di.observe
import com.maksimowiczm.findmyip._2.network.AddressStatus
import com.maksimowiczm.findmyip._2.network.ConnectivityObserver
import com.maksimowiczm.findmyip._2.network.NetworkAddressDataSource
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.database.AddressEntityDao
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HistoryManagerImpl(
    private val dataStore: DataStore<Preferences>,
    private val dao: AddressEntityDao,
    private val ipv4DataSource: NetworkAddressDataSource,
    private val ipv6DataSource: NetworkAddressDataSource,
    private val connectivityObserver: ConnectivityObserver
) : HistoryManager {
    /**
     * Observes the address for both IPv4 and IPv6 and saves them to the database.
     */
    override suspend fun run() {
        Logger.d(TAG) { "Executing run" }

        coroutineScope {
            launch { run(InternetProtocolVersion.IPv4) }
            launch { run(InternetProtocolVersion.IPv6) }
        }
    }

    /**
     * Retrieves the address for both IPv4 and IPv6 and saves them to the database.
     */
    override suspend fun once() {
        Logger.d(TAG) { "Executing once" }

        coroutineScope {
            launch { once(InternetProtocolVersion.IPv4) }
            launch { once(InternetProtocolVersion.IPv6) }
        }
    }

    /**
     * Observes the address for the given [internetProtocolVersion] and saves it to the database.
     */
    private suspend fun run(internetProtocolVersion: InternetProtocolVersion) {
        val preferenceKey = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> PreferenceKeys.ipv4Enabled
            InternetProtocolVersion.IPv6 -> PreferenceKeys.ipv6Enabled
        }

        combine(
            dataStore.observe(preferenceKey),
            dataStore.observe(PreferenceKeys.historyEnabled)
        ) { ipvEnabled, historyEnabled ->
            if (ipvEnabled != true || historyEnabled != true) {
                return@combine
            }

            val source = when (internetProtocolVersion) {
                InternetProtocolVersion.IPv4 -> ipv4DataSource
                InternetProtocolVersion.IPv6 -> ipv6DataSource
            }

            source
                .observeAddress()
                .mapNotNull { it as? AddressStatus.Success }
                .collect {
                    val (ip, networkType) = it.address

                    val address = Address(
                        ip = ip,
                        networkType = networkType,
                        date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                        protocolVersion = internetProtocolVersion
                    )

                    insertAddress(address)
                }
        }.collect {}
    }

    private suspend fun once(internetProtocolVersion: InternetProtocolVersion) {
        val preferenceKey = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> PreferenceKeys.ipv4Enabled
            InternetProtocolVersion.IPv6 -> PreferenceKeys.ipv6Enabled
        }

        if (dataStore.get(preferenceKey) != true) {
            return
        }

        if (dataStore.get(PreferenceKeys.historyEnabled) != true) {
            return
        }

        val source = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> ipv4DataSource
            InternetProtocolVersion.IPv6 -> ipv6DataSource
        }

        // Wait for the first non-null result
        val result = source
            .observeAddress()
            .mapNotNull { it as? AddressStatus.Success }
            .first()

        val (ip, networkType) = result.address

        val address = Address(
            ip = ip,
            networkType = networkType,
            date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            protocolVersion = internetProtocolVersion
        )

        insertAddress(address)
    }

    private suspend fun insertAddress(address: Address) {
        Logger.d(TAG) { "Inserting address: $address" }

        val shouldInsert = when (address.networkType) {
            NetworkType.WIFI -> dataStore.get(PreferenceKeys.saveWifiHistory) ?: false
            NetworkType.MOBILE -> dataStore.get(PreferenceKeys.saveMobileHistory) ?: false
            NetworkType.VPN -> dataStore.get(PreferenceKeys.saveVpnHistory) ?: false
            // If the network type is unknown, we check if the available network types is empty.
            // If so it means that network type will always be unknown.
            null -> connectivityObserver.availableNetworkTypes.isEmpty()
        }

        if (!shouldInsert) {
            Logger.d(TAG) { "Skipping address insertion" }
            return
        }

        val entity = address.toEntity()
        if (dataStore.get(PreferenceKeys.historySaveDuplicates) == true) {
            Logger.d(TAG) { "Inserting address with duplicates" }
            dao.insertAddressIfOlderThanLatest(entity, 10 * 1000)
        } else {
            Logger.d(TAG) { "Inserting address without duplicates" }
            dao.insertIfDistinct(entity)
        }
    }

    private companion object {
        const val TAG = "HistoryManagerImpl"
    }
}
