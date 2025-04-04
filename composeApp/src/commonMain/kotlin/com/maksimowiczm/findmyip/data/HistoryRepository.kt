package com.maksimowiczm.findmyip.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import com.maksimowiczm.findmyip.domain.ObserveHistoryUseCase
import com.maksimowiczm.findmyip.domain.ObserveHistoryUseCase.HistoryItem
import com.maksimowiczm.findmyip.domain.ShouldShowHistoryUseCase
import com.maksimowiczm.findmyip.infrastructure.di.observe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HistoryRepository(database: FindMyIpDatabase, private val dataStore: DataStore<Preferences>) :
    ObserveHistoryUseCase,
    ShouldShowHistoryUseCase {
    private val addressDao = database.addressDao

    override fun observeHistory(protocol: InternetProtocolVersion?): Flow<PagingData<HistoryItem>> =
        Pager(
            config = PagingConfig(
                pageSize = 30
            )
        ) {
            addressDao.observeAddresses(protocol)
        }.flow.map { data ->
            data.map {
                val date = Instant
                    .fromEpochMilliseconds(it.timestamp)
                    .toLocalDateTime(TimeZone.currentSystemDefault())

                HistoryItem(
                    id = it.id,
                    ip = it.ip,
                    date = date,
                    protocol = it.internetProtocolVersion
                )
            }
        }

    override fun shouldShowHistory(protocol: InternetProtocolVersion) = when (protocol) {
        InternetProtocolVersion.IPv4 -> shouldShowIpv4()
        InternetProtocolVersion.IPv6 -> shouldShowIpv6()
    }

    private fun shouldShowIpv4() = combine(
        dataStore.observe(PreferenceKeys.ipv4Enabled).map { it ?: false },
        addressDao.isNotEmpty(InternetProtocolVersion.IPv4)
    ) { ipv4Enabled, isNotEmpty ->
        if (!isNotEmpty) {
            ipv4Enabled
        } else {
            true
        }
    }

    private fun shouldShowIpv6() = combine(
        dataStore.observe(PreferenceKeys.ipv6Enabled).map { it ?: false },
        addressDao.isNotEmpty(InternetProtocolVersion.IPv6)
    ) { ipv6Enabled, isNotEmpty ->
        if (!isNotEmpty) {
            ipv6Enabled
        } else {
            true
        }
    }
}
