package com.maksimowiczm.findmyip.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class AddressRefreshWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val historyManager: HistoryManager
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        historyManager.once()

        return Result.success()
    }

    companion object {
        const val TAG = "AddressRefreshWorker"

        fun cancelAndCreatePeriodicWorkRequest(workManager: WorkManager, intervalInMinutes: Long) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<AddressRefreshWorker>(
                repeatInterval = intervalInMinutes,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag(TAG)
                .build()

            workManager.enqueueUniquePeriodicWork(
                uniqueWorkName = TAG,
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request = request
            )
        }

        fun cancelPeriodicWorkRequest(workManager: WorkManager) {
            workManager.cancelAllWorkByTag(TAG)
        }
    }
}
