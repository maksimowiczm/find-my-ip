package com.maksimowiczm.findmyip.application.usecase

import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentIp4AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import kotlinx.coroutines.flow.Flow

fun interface ObserveCurrentIp4AddressUseCase {
    fun observe(): Flow<AddressStatus<Ip4Address>>
}

internal class ObserveCurrentIp4AddressUseCaseImpl(
    private val currentIp4AddressLocalDataSource: CurrentIp4AddressLocalDataSource
) : ObserveCurrentIp4AddressUseCase {
    override fun observe(): Flow<AddressStatus<Ip4Address>> =
        currentIp4AddressLocalDataSource.observeIp4()
}
