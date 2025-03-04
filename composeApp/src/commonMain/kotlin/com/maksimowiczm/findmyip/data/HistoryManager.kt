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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
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
     * Observes the address for the given [internetProtocolVersion] and saves it to the database.
     */
    private suspend fun observe(internetProtocolVersion: InternetProtocolVersion) {
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

            source.observeAddress().collect { result ->
                if (result.isFailure) {
                    return@collect
                }

                val (ip, networkType) = result.getOrNull() ?: return@collect

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

    private suspend fun insertAddress(address: Address) {
        val shouldInsert = when (address.networkType) {
            NetworkType.WIFI -> dataStore.get(PreferenceKeys.saveWifiHistory) ?: false
            NetworkType.MOBILE -> dataStore.get(PreferenceKeys.saveMobileHistory) ?: false
            NetworkType.VPN -> dataStore.get(PreferenceKeys.saveVpnHistory) ?: false
            null -> false
        }

        if (!shouldInsert) {
            return
        }

        val saveDuplicates = dataStore.get(PreferenceKeys.historySaveDuplicates) ?: false

        if (saveDuplicates) {
            dao.insertAddress(addressEntity = address.toEntity())
        } else {
            dao.insertIfDistinct(addressEntity = address.toEntity())
        }
    }
}
