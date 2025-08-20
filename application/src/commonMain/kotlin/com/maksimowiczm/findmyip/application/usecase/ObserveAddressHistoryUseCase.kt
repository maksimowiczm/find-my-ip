package com.maksimowiczm.findmyip.application.usecase

import androidx.paging.PagingData
import androidx.paging.filter
import com.maksimowiczm.findmyip.application.infrastructure.local.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentIp4AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentIp6AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
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
     * @param ipv4 Whether to include IPv4 addresses in the history.
     * @param ipv6 Whether to include IPv6 addresses in the history.
     * @return A flow of [PagingData] containing the filtered address history.
     */
    fun observe(ipv4: Boolean, ipv6: Boolean): Flow<PagingData<AddressHistory>>
}

@OptIn(ExperimentalCoroutinesApi::class)
internal class ObserveAddressHistoryUseCaseImpl(
    private val addressHistoryLocalDataSource: AddressHistoryLocalDataSource,
    private val currentIp4: CurrentIp4AddressLocalDataSource,
    private val currentIp6: CurrentIp6AddressLocalDataSource,
) : ObserveAddressHistoryUseCase {
    override fun observe(ipv4: Boolean, ipv6: Boolean): Flow<PagingData<AddressHistory>> =
        addressHistoryLocalDataSource.observeHistory(ipv4 = ipv4, ipv6 = ipv6).filterCurrent()

    private fun Flow<PagingData<AddressHistory>>.filterCurrent(): Flow<PagingData<AddressHistory>> =
        combine(
                currentIp4.observeIp4().observeWithInitialNull(),
                currentIp6.observeIp6().observeWithInitialNull(),
            ) { ip4, ip6 ->
                val current4addr = (ip4 as? AddressStatus.Success<Ip4Address>)?.value
                val current6addr = (ip6 as? AddressStatus.Success<Ip6Address>)?.value

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
