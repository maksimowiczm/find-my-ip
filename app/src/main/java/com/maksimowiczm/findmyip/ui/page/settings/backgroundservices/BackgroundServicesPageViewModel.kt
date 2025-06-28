package com.maksimowiczm.findmyip.ui.page.settings.backgroundservices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.domain.backgroundservices.PeriodicWorker
import com.maksimowiczm.findmyip.ext.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class BackgroundServicesPageViewModel(private val periodicWorker: PeriodicWorker) : ViewModel() {
    val periodicWorkEnabled = periodicWorker.isRunning.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2_000),
        initialValue = runBlocking { periodicWorker.isRunning.first() }
    )

    fun onTogglePeriodicWork(enabled: Boolean) = launch {
        if (enabled) {
            periodicWorker.start()
        } else {
            periodicWorker.stop()
        }
    }
}
