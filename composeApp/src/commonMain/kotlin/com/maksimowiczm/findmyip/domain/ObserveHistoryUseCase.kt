package com.maksimowiczm.findmyip.domain

import androidx.paging.PagingData
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface ObserveHistoryUseCase {
    data class HistoryItem(
        val id: Long,
        val ip: String,
        val date: LocalDateTime,
        val protocol: InternetProtocolVersion
    )

    fun observeHistory(protocol: InternetProtocolVersion?): Flow<PagingData<HistoryItem>>
}
