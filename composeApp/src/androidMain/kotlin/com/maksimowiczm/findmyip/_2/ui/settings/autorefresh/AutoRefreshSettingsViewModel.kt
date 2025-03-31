@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.ui.settings.autorefresh

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip._2.data.AndroidPreferenceKeys.autoRefreshSettingsEnabledKey
import com.maksimowiczm.findmyip._2.infrastructure.di.get
import com.maksimowiczm.findmyip._2.infrastructure.di.observe
import com.maksimowiczm.findmyip._2.infrastructure.di.set
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AutoRefreshSettingsViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {
    val enabled = dataStore.observe(autoRefreshSettingsEnabledKey)
        .map { it ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = runBlocking { dataStore.get(autoRefreshSettingsEnabledKey) ?: false }
        )

    fun setEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.set(autoRefreshSettingsEnabledKey to enabled)
        }
    }
}
