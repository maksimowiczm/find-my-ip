package com.maksimowiczm.findmyip.domain.backgroundservices

import kotlinx.coroutines.flow.Flow

/**
 * Interface for a periodic worker that can be started and stopped.
 */
interface PeriodicWorker {
    val isRunning: Flow<Boolean>
    suspend fun start()
    suspend fun stop()
}
