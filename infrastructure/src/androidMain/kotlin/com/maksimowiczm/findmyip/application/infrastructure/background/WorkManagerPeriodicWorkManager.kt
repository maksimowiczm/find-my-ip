package com.maksimowiczm.findmyip.application.infrastructure.background

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import com.maksimowiczm.findmyip.application.usecase.RefreshAddressUseCase
import com.maksimowiczm.findmyip.domain.entity.InternetProtocolVersion
import com.maksimowiczm.findmyip.shared.log.Logger
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.getValue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

internal class WorkManagerPeriodicWorkManager(
    private val context: Context,
    private val logger: Logger,
) : PeriodicWorkManager {

    private val workManager: WorkManager
        get() = WorkManager.getInstance(context)

    override val isRunning: Flow<Boolean>
        get() =
            workManager.getWorkInfosByTagFlow(AndroidPeriodicWorker.TAG).map { list ->
                if (list.size > 1) {
                    logger.w(TAG) { "More than one worker found!" }
                }

                val info = list.firstOrNull() ?: return@map false

                when (info.state) {
                    WorkInfo.State.ENQUEUED,
                    WorkInfo.State.RUNNING -> true

                    WorkInfo.State.SUCCEEDED,
                    WorkInfo.State.FAILED,
                    WorkInfo.State.BLOCKED,
                    WorkInfo.State.CANCELLED -> false
                }
            }

    override suspend fun start() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val request =
            PeriodicWorkRequestBuilder<AndroidPeriodicWorker>(
                    repeatInterval = 30,
                    repeatIntervalTimeUnit = TimeUnit.MINUTES,
                )
                .setConstraints(constraints)
                .setInitialDelay(Duration.ofMinutes(15))
                .addTag(AndroidPeriodicWorker.TAG)
                .build()

        workManager
            .enqueueUniquePeriodicWork(
                AndroidPeriodicWorker.TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                request,
            )
            .await()
    }

    override suspend fun stop() {
        workManager.cancelAllWorkByTag(AndroidPeriodicWorker.TAG).await()
    }

    private companion object {
        const val TAG = "WorkManagerPeriodicWorkManager"
    }
}

class AndroidPeriodicWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {
    private val logger: Logger by inject()
    private val refreshIp4: RefreshAddressUseCase by inject(named(InternetProtocolVersion.IPV4))
    private val refreshIp6: RefreshAddressUseCase by inject(named(InternetProtocolVersion.IPV6))

    override suspend fun doWork(): Result {
        logger.d(TAG) { "Periodic worker started" }

        coroutineScope {
            launch {
                refreshIp4.refresh()
                logger.d(TAG) { "IPv4 address refreshed" }
            }
            launch {
                refreshIp6.refresh()
                logger.d(TAG) { "IPv6 address refreshed" }
            }
        }

        logger.d(TAG) { "Periodic worker finished" }

        return Result.success()
    }

    companion object {
        const val TAG = "AndroidPeriodicWorker"
    }
}
