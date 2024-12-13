package com.maksimowiczm.findmyip.settings.backgroundworker

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.maksimowiczm.findmyip.data.Keys
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import com.maksimowiczm.findmyip.data.worker.AddressHistoryBackgroundWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class WorkerSettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    @ApplicationContext applicationContext: Context
) : ViewModel() {
    val workerStatus = WorkManager
        .getInstance(applicationContext)
        .getWorkInfosByTagFlow(AddressHistoryBackgroundWorker.Companion.TAG)
        .map { infos ->
            if (infos.size > 1) {
                Log.w("WorkerSettingsViewModel", "More than one worker with the same tag")
            }

            infos.firstOrNull()?.state
        }.filterNotNull()

    val intervals = arrayOf<Long>(15, 30, 60, 120, 240, 480, 720, 1440)
    val localEnabled = MutableStateFlow(false)

    val workerEnabledState: StateFlow<Boolean> =
        combine(
            userPreferencesRepository.observe(Keys.run_background_worker),
            localEnabled
        ) { runWorker, localEnabled ->
            runWorker == true || localEnabled
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = false
        )
    val workerIntervalIndex: StateFlow<Int> =
        userPreferencesRepository.observe(Keys.background_worker_interval)
            .map { intervals.indexOf(it) }
            .map { if (it == -1) 0 else it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(2000),
                initialValue = 0
            )

    private var setupJob: Job? = null
    fun enableWorker(intervalIndex: Int) {
        setupJob?.cancel()
        setupJob = viewModelScope.launch {
            localEnabled.emit(true)
            delay(500)
            userPreferencesRepository.set(Keys.run_background_worker, true)
            userPreferencesRepository.set(Keys.background_worker_interval, intervals[intervalIndex])
        }
    }

    fun disableWorker() {
        setupJob?.cancel()
        viewModelScope.launch {
            localEnabled.emit(false)
            userPreferencesRepository.set(Keys.run_background_worker, false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        setupJob?.cancel()
    }
}
