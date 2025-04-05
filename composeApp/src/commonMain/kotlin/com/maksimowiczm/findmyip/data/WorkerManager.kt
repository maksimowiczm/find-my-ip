package com.maksimowiczm.findmyip.data

import kotlinx.coroutines.flow.Flow

expect class WorkerManager {
    val isEnabled: Flow<Boolean>

    suspend fun cancel()
    suspend fun start()
}
