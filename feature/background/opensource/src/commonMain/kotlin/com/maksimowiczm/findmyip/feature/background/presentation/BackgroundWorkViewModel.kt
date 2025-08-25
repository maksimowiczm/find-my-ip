package com.maksimowiczm.findmyip.feature.background.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.application.infrastructure.background.PeriodicWorkManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class BackgroundWorkViewModel(private val periodicWorkManager: PeriodicWorkManager) :
    ViewModel() {
    val periodicWorkerRunning =
        periodicWorkManager.isRunning.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = runBlocking { periodicWorkManager.isRunning.first() },
        )

    fun togglePeriodicWorker(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled) {
                periodicWorkManager.start()
            } else {
                periodicWorkManager.stop()
            }
        }
    }
}
