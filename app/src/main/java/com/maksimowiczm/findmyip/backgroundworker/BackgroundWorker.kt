package com.maksimowiczm.findmyip.backgroundworker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.michaelbull.result.getOrElse
import com.maksimowiczm.findmyip.domain.ObserveCurrentAddressUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class BackgroundWorker @AssistedInject constructor(
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
    }
}
