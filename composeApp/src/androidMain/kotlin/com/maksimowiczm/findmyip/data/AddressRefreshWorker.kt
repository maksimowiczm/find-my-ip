package com.maksimowiczm.findmyip.data

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AddressRefreshWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val addressRepository: AddressRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        coroutineScope {
            launch { refreshAddress(InternetProtocolVersion.IPv4) }
            launch { refreshAddress(InternetProtocolVersion.IPv6) }
        }

        return Result.success()
    }

    private suspend fun refreshAddress(internetProtocolVersion: InternetProtocolVersion) {
        val status = addressRepository.refreshAddressPersist(internetProtocolVersion)
        Log.d(TAG, "Protocol: $internetProtocolVersion, Status: $status")
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
