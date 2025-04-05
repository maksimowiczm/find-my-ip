package com.maksimowiczm.findmyip.data

import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.map

actual class WorkerManager(private val workManager: WorkManager) {
    actual val isEnabled = workManager
        .getWorkInfosByTagFlow(AddressRefreshWorker.TAG)
        .map { infos ->
            when (infos.firstOrNull()?.state) {
                null,
                WorkInfo.State.SUCCEEDED,
                WorkInfo.State.FAILED,
                WorkInfo.State.BLOCKED,
                WorkInfo.State.CANCELLED -> false

                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING -> true
            }
        }

    actual suspend fun cancel() {
        AddressRefreshWorker.cancelPeriodicWorkRequest(workManager)
    }

    actual suspend fun start() {
        AddressRefreshWorker.cancelAndCreatePeriodicWorkRequest(workManager)
    }
}
