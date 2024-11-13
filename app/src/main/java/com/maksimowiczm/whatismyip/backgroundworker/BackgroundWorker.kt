package com.maksimowiczm.whatismyip.backgroundworker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.michaelbull.result.mapBoth
import com.maksimowiczm.whatismyip.data.Keys
import com.maksimowiczm.whatismyip.data.repository.PublicAddressRepository
import com.maksimowiczm.whatismyip.data.repository.UserPreferencesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class BackgroundWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val publicAddressRepository: PublicAddressRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        // Make sure we are allowed to save the history
        if (userPreferencesRepository.get(Keys.save_history).firstOrNull() != true) {
            return Result.success()
        }

        publicAddressRepository.refreshCurrentAddress().mapBoth(
            success = { publicAddressRepository.insertIfDistinct(it) },
            failure = { Log.w(TAG, "Failed to refresh current address") }
        )

        return Result.success()
    }

    companion object {
        const val TAG = "BackgroundWorker"
    }
}
