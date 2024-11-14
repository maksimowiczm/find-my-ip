package com.maksimowiczm.findmyip

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.maksimowiczm.findmyip.backgroundworker.BackgroundWorker
import com.maksimowiczm.findmyip.data.Keys
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn

@HiltAndroidApp
class Application : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository
    private var workerJob: Job? = null

    override fun onCreate() {
        super.onCreate()

        workerJob = combine(
            userPreferencesRepository
                .get(Keys.run_background_worker).drop(1).distinctUntilChanged(),
            userPreferencesRepository
                .get(Keys.background_worker_interval).drop(1).distinctUntilChanged()
        ) { runWorker, interval ->
            val workManager = WorkManager.getInstance(this@Application)

            if (runWorker != true || interval == null) {
                Log.d(TAG, "Cancelling background worker")
                workManager.cancelAllWorkByTag(BackgroundWorker.TAG)
                return@combine
            }

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<BackgroundWorker>(interval, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(BackgroundWorker.TAG)
                .build()

            Log.d(TAG, "Enqueuing background worker")
            workManager.enqueueUniquePeriodicWork(
                BackgroundWorker.TAG,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request
            )
        }.launchIn(CoroutineScope(Dispatchers.Default + SupervisorJob()))
    }

    override fun onTerminate() {
        super.onTerminate()
        workerJob?.cancel()
    }

    companion object {
        private const val TAG = "Application"
    }
}
