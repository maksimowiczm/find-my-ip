package com.maksimowiczm.findmyip.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.model.NetworkType
import com.maksimowiczm.findmyip.data.model.toEntity
import com.maksimowiczm.findmyip.database.AddressEntityDao
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HistoryManager(
    private val dataStore: DataStore<Preferences>,
    private val dao: AddressEntityDao,
    private val ipv4DataSource: NetworkAddressDataSource,
    private val ipv6DataSource: NetworkAddressDataSource
) {
    /**
     * Observes the address for both IPv4 and IPv6 and saves them to the database.
     */
    suspend fun run() {
        coroutineScope {
            launch { run(InternetProtocolVersion.IPv4) }
            launch { run(InternetProtocolVersion.IPv6) }
        }
    }

    /**
     * Retrieves the address for both IPv4 and IPv6 and saves them to the database.
     */
    suspend fun once() {
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
                .mapNotNull { it.getOrNull() }
                .collect {
                    val (ip, networkType) = it

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

        val result = source.observeAddress().first()

        val (ip, networkType) = result.getOrNull() ?: return

        val address = Address(
            ip = ip,
            networkType = networkType,
            date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            protocolVersion = internetProtocolVersion
        )

        insertAddress(address)
    }

    private suspend fun insertAddress(address: Address) {
        val shouldInsert = when (address.networkType) {
            NetworkType.WIFI -> dataStore.get(PreferenceKeys.saveWifiHistory)
            NetworkType.MOBILE -> dataStore.get(PreferenceKeys.saveMobileHistory)
            NetworkType.VPN -> dataStore.get(PreferenceKeys.saveVpnHistory)
            else -> false
        } ?: false

        if (!shouldInsert) return

        val entity = address.toEntity()
        if (dataStore.get(PreferenceKeys.historySaveDuplicates) == true) {
            dao.insertAddress(entity)
        } else {
            dao.insertIfDistinct(entity)
        }
    }
}
