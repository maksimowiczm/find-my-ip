package com.maksimowiczm.findmyip.shared.core.application.usecase

import com.maksimowiczm.findmyip.shared.core.application.infrastructure.local.CurrentAddressLocalDataSource
import com.maksimowiczm.findmyip.shared.core.domain.AddressStatus
import com.maksimowiczm.findmyip.shared.core.domain.IpAddress
import kotlinx.coroutines.flow.Flow

fun interface ObserveCurrentIpAddressUseCase<A : IpAddress> {
    fun observe(): Flow<AddressStatus<A>>
}

internal class ObserveCurrentIpAddressUseCaseImpl<A : IpAddress>(
    private val currentAddressLocalDataSource: CurrentAddressLocalDataSource<A>
) : ObserveCurrentIpAddressUseCase<A> {
    override fun observe(): Flow<AddressStatus<A>> = currentAddressLocalDataSource.observe()
}
