@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import co.touchlab.kermit.Logger
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

class AddressRefreshWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val historyManager: HistoryManager
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result = try {
        withTimeout(5_000) {
            historyManager.once()
        }
        Logger.d(TAG) { "Address refreshed" }
        Result.success()
    } catch (e: TimeoutCancellationException) {
        Logger.e(TAG, e) { "Operation timed out" }
        Result.failure()
    }

    companion object {
        const val TAG = "AddressRefreshWorker"

        suspend fun cancelAndCreatePeriodicWorkRequest(
            workManager: WorkManager,
            intervalInMinutes: Long
        ) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<AddressRefreshWorker>(
                repeatInterval = intervalInMinutes,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
                .setInitialDelay(
                    duration = intervalInMinutes,
                    timeUnit = TimeUnit.MINUTES
                )
                .setConstraints(constraints)
                .addTag(TAG)
                .build()

            val operation = workManager.enqueueUniquePeriodicWork(
                uniqueWorkName = TAG,
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request = request
            )

            operation.await()
        }

        fun cancelPeriodicWorkRequest(workManager: WorkManager) {
            workManager.cancelAllWorkByTag(TAG)
        }
    }
}
