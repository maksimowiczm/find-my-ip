package com.maksimowiczm.findmyip.application.usecase

import androidx.paging.PagingData
import com.maksimowiczm.findmyip.application.infrastructure.local.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import kotlinx.coroutines.flow.Flow

fun interface ObserveAddressHistoryUseCase {
    fun observe(): Flow<PagingData<AddressHistory>>
}

internal class ObserveAddressHistoryUseCaseImpl(
    private val addressHistoryLocalDataSource: AddressHistoryLocalDataSource
) : ObserveAddressHistoryUseCase {
    override fun observe(): Flow<PagingData<AddressHistory>> =
        addressHistoryLocalDataSource.observeHistory()
}
