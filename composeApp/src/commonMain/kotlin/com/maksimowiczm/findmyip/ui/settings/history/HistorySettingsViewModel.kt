package com.maksimowiczm.findmyip.ui.settings.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HistorySettingsViewModel(
    private val dataStore: DataStore<Preferences>,
    private val addressRepository: AddressRepository
) : ViewModel() {
    val isEnabled = dataStore
        .observe(PreferenceKeys.historyEnabled)
        .map { it ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = runBlocking { dataStore.get(PreferenceKeys.historyEnabled) ?: false }
        )

    fun onEnableChange(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.set(PreferenceKeys.historyEnabled to isEnabled)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            addressRepository.clearHistory()
        }
    }
}
