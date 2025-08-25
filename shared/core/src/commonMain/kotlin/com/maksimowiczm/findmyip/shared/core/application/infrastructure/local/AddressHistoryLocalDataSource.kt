package com.maksimowiczm.findmyip.shared.core.application.infrastructure.local

import androidx.paging.PagingData
import com.maksimowiczm.findmyip.shared.core.domain.AddressHistory
import kotlinx.coroutines.flow.Flow

interface AddressHistoryLocalDataSource {
    fun observeHistory(
        query: String?,
        ipv4: Boolean,
        ipv6: Boolean,
    ): Flow<PagingData<AddressHistory>>

    suspend fun saveHistory(history: AddressHistory)

    suspend fun getLatestIp4Address(): AddressHistory?

    suspend fun getLatestIp6Address(): AddressHistory?
}
