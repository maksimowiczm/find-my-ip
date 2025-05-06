package com.maksimowiczm.findmyip.domain.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey

object OnboardingPreferences {
    val onboardingCompleted = booleanPreferencesKey("onboarding_completed")
}
