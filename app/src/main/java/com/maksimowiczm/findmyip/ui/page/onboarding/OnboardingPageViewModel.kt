package com.maksimowiczm.findmyip.ui.page.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.maksimowiczm.findmyip.domain.backgroundservices.ForegroundService
import com.maksimowiczm.findmyip.domain.backgroundservices.PeriodicWorker
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences
import com.maksimowiczm.findmyip.domain.preferences.OnboardingPreferences
import com.maksimowiczm.findmyip.ext.launch

class OnboardingPageViewModel(
    private val dataStore: DataStore<Preferences>,
    private val periodicWorker: PeriodicWorker,
    private val foregroundService: ForegroundService
) : ViewModel() {

    private var enableBackgroundTracking: Boolean? = null

    fun enableBackgroundTracking() {
        enableBackgroundTracking = true
    }

    fun skipBackgroundTracking() {
        enableBackgroundTracking = false
    }

    private var enableNotifications: Boolean? = null

    fun enableNotifications() {
        enableNotifications = true
    }

    fun skipNotifications() {
        enableNotifications = false
    }

    fun onFinish() = launch {
        if (enableBackgroundTracking == true) {
            periodicWorker.start()
        }

        if (enableNotifications == true && enableBackgroundTracking == true) {
            foregroundService.start()
        }

        dataStore.edit {
            if (enableNotifications == true) {
                with(NotificationPreferences) {
                    it.enableAll()
                }
            }

            it[OnboardingPreferences.onboardingCompleted] = true
        }
    }
}
