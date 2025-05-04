package com.maksimowiczm.findmyip.infrastructure.android

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import com.maksimowiczm.findmyip.domain.backgroundservices.PeriodicWorker
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.usecase.BackgroundRefreshUseCase
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeout
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class AndroidPeriodicWorker(private val workManager: WorkManager) : PeriodicWorker {
    override val isRunning: Flow<Boolean>
        get() = workManager
            .getWorkInfosByTagFlow(WORK_TAG)
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

    override suspend fun start() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val request = PeriodicWorkRequestBuilder<AddressRefreshWorker>(
            repeatInterval = 30,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).setConstraints(constraints).addTag(WORK_TAG).build()

        val operation = workManager.enqueueUniquePeriodicWork(
            WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )

        operation.await()
    }

    override suspend fun stop() {
        workManager.cancelAllWorkByTag(WORK_TAG)
    }

    companion object {
        const val WORK_TAG = "AddressRefreshWorker"
    }
}

class AddressRefreshWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters),
    KoinComponent {

    private val backgroundRefresh4 by inject<BackgroundRefreshUseCase>(named(InternetProtocol.IPv4))
    private val backgroundRefresh6 by inject<BackgroundRefreshUseCase>(named(InternetProtocol.IPv6))

    override suspend fun doWork(): Result = coroutineScope {
        Log.d(TAG, "Starting address refresh")

        val (ipv4Result, ipv6Result) = awaitAll(
            async { doWork(InternetProtocol.IPv4) },
            async { doWork(InternetProtocol.IPv6) }
        )

        if (ipv4Result.isFailure && ipv6Result.isFailure) {
            Log.e(TAG, "Failed to refresh both IPv4 and IPv6 addresses")
            Log.e(TAG, "IPv4 error", ipv4Result.exceptionOrNull())
            Log.e(TAG, "IPv6 error", ipv6Result.exceptionOrNull())
            return@coroutineScope Result.failure()
        }

        Log.d(TAG, "Successfully refreshed addresses")
        return@coroutineScope Result.success()
    }

    suspend fun doWork(protocol: InternetProtocol) = runCatching {
        withTimeout(10_000) {
            when (protocol) {
                InternetProtocol.IPv4 -> backgroundRefresh4.refresh()
                InternetProtocol.IPv6 -> backgroundRefresh6.refresh()
            }.getOrThrow()
        }
    }.onFailure {
        Log.w(TAG, "Failed to refresh $protocol address", it)
    }.onSuccess {
        Log.d(TAG, "Successfully refreshed $protocol address")
    }

    private companion object {
        const val TAG = "AddressRefreshWorker"
    }
}
