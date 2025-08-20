package com.maksimowiczm.findmyip.application.infrastructure.local

import androidx.paging.PagingData
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import kotlinx.coroutines.flow.Flow

interface AddressHistoryLocalDataSource {
    fun observeHistory(ipv4: Boolean, ipv6: Boolean): Flow<PagingData<AddressHistory>>

    suspend fun saveHistory(history: AddressHistory)

    suspend fun getLatestIp4Address(): AddressHistory?

    suspend fun getLatestIp6Address(): AddressHistory?
}
