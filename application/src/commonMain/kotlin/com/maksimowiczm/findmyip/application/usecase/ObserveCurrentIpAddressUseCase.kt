package com.maksimowiczm.findmyip.application.usecase

import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentAddressLocalDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.IpAddress
import kotlinx.coroutines.flow.Flow

fun interface ObserveCurrentIpAddressUseCase<A : IpAddress> {
    fun observe(): Flow<AddressStatus<A>>
}

internal class ObserveCurrentIpAddressUseCaseImpl<A : IpAddress>(
    private val currentAddressLocalDataSource: CurrentAddressLocalDataSource<A>
) : ObserveCurrentIpAddressUseCase<A> {
    override fun observe(): Flow<AddressStatus<A>> = currentAddressLocalDataSource.observe()
}
