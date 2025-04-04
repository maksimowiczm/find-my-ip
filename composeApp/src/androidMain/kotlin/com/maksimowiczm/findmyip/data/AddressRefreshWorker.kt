package com.maksimowiczm.findmyip.data

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
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.domain.RefreshAndGetIfLatestUseCase
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.TimeoutCancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddressRefreshWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters),
    KoinComponent {
    private val notificationHelper: NotificationHelper by inject()

    override suspend fun doWork(): Result = try {
        Logger.d(TAG) { "Address refreshed" }

        notificationHelper.notifyAddressChange(
            Address.Success(
                ip = "Test",
                networkType = com.maksimowiczm.findmyip.data.model.NetworkType.UNKNOWN,
                protocol = InternetProtocolVersion.IPv4
            )
        )

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
                .setConstraints(constraints)
                .addTag(TAG)
                .build()

            val operation = workManager.enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request
            )

            operation.await()
        }

        fun cancelPeriodicWorkRequest(workManager: WorkManager) {
            workManager.cancelAllWorkByTag(TAG)
        }
    }
}

private suspend operator fun RefreshAndGetIfLatestUseCase.invoke(
    protocol: InternetProtocolVersion
) = refreshAndGetIfLatest(protocol)
