package com.maksimowiczm.findmyip.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.github.michaelbull.result.getOrElse
import com.maksimowiczm.findmyip.domain.ObserveCurrentAddressUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first

@HiltWorker
class AddressHistoryBackgroundWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val observeCurrentAddressUseCase: ObserveCurrentAddressUseCase
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        observeCurrentAddressUseCase().first().getOrElse {
            Log.w(TAG, "Failed to refresh current address")
            return Result.failure()
        }

        return Result.success()
    }

    companion object {
        const val TAG = "BackgroundWorker"

        fun createPeriodicWorkRequest(workManager: WorkManager, intervalInMinutes: Long) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<AddressHistoryBackgroundWorker>(
                intervalInMinutes,
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag(TAG)
                .build()

            workManager.enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request
            )
        }

        fun cancelPeriodicWorkRequest(workManager: WorkManager) {
            workManager.cancelAllWorkByTag(TAG)
        }
    }
}
