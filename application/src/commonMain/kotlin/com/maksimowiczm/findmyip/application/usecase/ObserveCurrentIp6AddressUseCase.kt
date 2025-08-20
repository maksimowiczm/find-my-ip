package com.maksimowiczm.findmyip.application.usecase

import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentIp6AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import kotlinx.coroutines.flow.Flow

fun interface ObserveCurrentIp6AddressUseCase {
    fun observe(): Flow<AddressStatus<Ip6Address>>
}

internal class ObserveCurrentIp6AddressUseCaseImpl(
    private val currentIp6AddressLocalDataSource: CurrentIp6AddressLocalDataSource
) : ObserveCurrentIp6AddressUseCase {
    override fun observe(): Flow<AddressStatus<Ip6Address>> =
        currentIp6AddressLocalDataSource.observeIp6()
}
