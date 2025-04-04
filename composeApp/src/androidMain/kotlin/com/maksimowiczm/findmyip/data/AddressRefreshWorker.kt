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
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.domain.RefreshAndGetIfLatestUseCase
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeout
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddressRefreshWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters),
    KoinComponent {
    private val notificationHelper: NotificationHelper by inject()
    private val refreshAndGetIfLatestUseCase: RefreshAndGetIfLatestUseCase by inject()

    override suspend fun doWork(): Result {
        coroutineScope {
            awaitAll(
                async { doWork(InternetProtocolVersion.IPv4) },
                async { doWork(InternetProtocolVersion.IPv6) }
            )
        }

        return Result.success()
    }

    private suspend fun doWork(protocol: InternetProtocolVersion) = try {
        val ipv4Result = withTimeout(5000) {
            refreshAndGetIfLatestUseCase(protocol)
        }

        with(ipv4Result) {
            when (this) {
                RefreshAndGetIfLatestUseCase.AddressResult.Disabled -> {
                    Logger.d(TAG) { "Address refresh disabled" }
                }

                is RefreshAndGetIfLatestUseCase.AddressResult.Error -> {
                    Logger.w(TAG, e) { "Error refreshing address" }
                }

                RefreshAndGetIfLatestUseCase.AddressResult.Skipped -> {
                    Logger.d(TAG) { "Address not changed" }
                }

                is RefreshAndGetIfLatestUseCase.AddressResult.Success -> {
                    notificationHelper.notifyAddressChange(address)
                }
            }
        }
    } catch (e: TimeoutCancellationException) {
        Logger.e(TAG, e) { "Operation timed out" }
    }

    companion object {
        const val TAG = "AddressRefreshWorker"

        suspend fun cancelAndCreatePeriodicWorkRequest(
            workManager: WorkManager,
            intervalInMinutes: Long
        ) {
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            val request = PeriodicWorkRequestBuilder<AddressRefreshWorker>(
                repeatInterval = intervalInMinutes,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            ).setConstraints(constraints).addTag(TAG).build()

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
