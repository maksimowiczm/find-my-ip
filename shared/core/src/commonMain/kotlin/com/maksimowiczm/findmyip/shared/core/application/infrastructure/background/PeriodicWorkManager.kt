package com.maksimowiczm.findmyip.shared.core.application.infrastructure.background

import kotlinx.coroutines.flow.Flow

interface PeriodicWorkManager {
    val isRunning: Flow<Boolean>

    suspend fun start()

    suspend fun stop()
}
