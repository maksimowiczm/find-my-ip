package com.maksimowiczm.findmyip.application.infrastructure

import androidx.paging.PagingData
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import kotlinx.coroutines.flow.Flow

interface AddressHistoryLocalDataSource {
    fun observeHistory(): Flow<PagingData<AddressHistory>>

    suspend fun saveHistory(history: AddressHistory)
}
