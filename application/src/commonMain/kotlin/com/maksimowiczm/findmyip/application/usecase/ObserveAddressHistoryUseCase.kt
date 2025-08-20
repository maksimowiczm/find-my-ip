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
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun interface ObserveAddressHistoryUseCase {
    fun observe(): Flow<PagingData<AddressHistory>>
}

@OptIn(ExperimentalCoroutinesApi::class)
internal class ObserveAddressHistoryUseCaseImpl(
    private val addressHistoryLocalDataSource: AddressHistoryLocalDataSource,
    private val currentIp4: CurrentIp4AddressLocalDataSource,
    private val currentIp6: CurrentIp6AddressLocalDataSource,
) : ObserveAddressHistoryUseCase {
    override fun observe(): Flow<PagingData<AddressHistory>> =
        combine(currentIp4.observeWithInitialNull(), currentIp6.observeWithInitialNull()) { ip4, ip6
                ->
                val current4addr = (ip4 as? AddressStatus.Success<Ip4Address>)?.value
                val current6addr = (ip6 as? AddressStatus.Success<Ip6Address>)?.value

                addressHistoryLocalDataSource.observeHistory().map { data ->
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

    private fun CurrentIp4AddressLocalDataSource.observeWithInitialNull():
        Flow<AddressStatus<Ip4Address>?> =
        observeIp4().map { it as AddressStatus<Ip4Address>? }.onStart { emit(null) }

    private fun CurrentIp6AddressLocalDataSource.observeWithInitialNull():
        Flow<AddressStatus<Ip6Address>?> =
        observeIp6().map { it as AddressStatus<Ip6Address>? }.onStart { emit(null) }
}

@OptIn(ExperimentalTime::class)
private fun LocalDateTime.toEpochSeconds(): Long =
    toInstant(TimeZone.currentSystemDefault()).epochSeconds
