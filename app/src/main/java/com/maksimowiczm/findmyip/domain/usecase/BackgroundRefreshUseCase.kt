package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.domain.source.AddressObserver

fun interface BackgroundRefreshUseCase {
    suspend fun refresh(): Result<Unit>
}

class BackgroundRefreshUseCaseImpl(
    private val addressObserver: AddressObserver,
    private val processAddressUseCase: ProcessAddressUseCase
) : BackgroundRefreshUseCase {
    override suspend fun refresh(): Result<Unit> = runCatching {
        val address = addressObserver.refresh().getOrThrow()
        processAddressUseCase.process(address)
    }
}
