package com.maksimowiczm.findmyip.domain.backgroundservices

import kotlinx.coroutines.flow.Flow

interface ForegroundService {
    val isRunning: Flow<Boolean>
    suspend fun start()
    suspend fun stop()
}
