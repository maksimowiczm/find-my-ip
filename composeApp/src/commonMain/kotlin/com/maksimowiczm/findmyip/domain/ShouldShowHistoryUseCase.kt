package com.maksimowiczm.findmyip.domain

import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.Flow

interface ShouldShowHistoryUseCase {
    fun shouldShowHistory(protocol: InternetProtocolVersion): Flow<Boolean>
}
