package com.maksimowiczm.findmyip.shared.core.application.usecase

import androidx.paging.PagingData
import androidx.paging.filter
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.local.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.local.CurrentAddressLocalDataSource
import com.maksimowiczm.findmyip.shared.core.domain.AddressHistory
import com.maksimowiczm.findmyip.shared.core.domain.AddressStatus
import com.maksimowiczm.findmyip.shared.core.domain.Ip4Address
import com.maksimowiczm.findmyip.shared.core.domain.Ip6Address
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.observeWithInitialNull
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun interface ObserveAddressHistoryUseCase {
    /**
     * Observes the address history, filtering out the current addresses.
     *
     * @param query The search query to filter the address history.
     * @param ipv4 Whether to include IPv4 addresses in the history.
     * @param ipv6 Whether to include IPv6 addresses in the history.
     * @return A flow of [PagingData] containing the filtered address history.
     */
    fun observe(query: String?, ipv4: Boolean, ipv6: Boolean): Flow<PagingData<AddressHistory>>
}

@OptIn(ExperimentalCoroutinesApi::class)
internal class ObserveAddressHistoryUseCaseImpl(
    private val addressHistoryLocalDataSource: AddressHistoryLocalDataSource,
    private val currentIp4: CurrentAddressLocalDataSource<Ip4Address>,
    private val currentIp6: CurrentAddressLocalDataSource<Ip6Address>,
) : ObserveAddressHistoryUseCase {
    override fun observe(
        query: String?,
        ipv4: Boolean,
        ipv6: Boolean,
    ): Flow<PagingData<AddressHistory>> =
        addressHistoryLocalDataSource
            .observeHistory(query = query, ipv4 = ipv4, ipv6 = ipv6)
            .filterCurrent()

    private fun Flow<PagingData<AddressHistory>>.filterCurrent(): Flow<PagingData<AddressHistory>> =
        combine(
                currentIp4.observe().observeWithInitialNull(),
                currentIp6.observe().observeWithInitialNull(),
            ) { ip4, ip6 ->
                val current4addr = (ip4 as? AddressStatus.Success<Ip4Address>)?.address
                val current6addr = (ip6 as? AddressStatus.Success<Ip6Address>)?.address

                map { data ->
                    data.filter {
                        when (it) {
                            is AddressHistory.Ipv4 ->
                                it.address != current4addr &&
                                    it.dateTime.toEpochSeconds() != ip4?.dateTime?.toEpochSeconds()

                            is AddressHistory.Ipv6 ->
                                it.address != current6addr &&
                                    it.dateTime.toEpochSeconds() != ip6?.dateTime?.toEpochSeconds()
                        }
                    }
                }
            }
            .flatMapLatest { it }
}

@OptIn(ExperimentalTime::class)
private fun LocalDateTime.toEpochSeconds(): Long =
    toInstant(TimeZone.currentSystemDefault()).epochSeconds
