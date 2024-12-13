package com.maksimowiczm.findmyip.settings.addresshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.Keys
import com.maksimowiczm.findmyip.data.repository.PublicAddressRepository
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class AddressHistorySettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val publicAddressRepository: PublicAddressRepository
) : ViewModel() {
    val saveHistoryState = userPreferencesRepository.observe(Keys.save_history).map {
        it == true
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = false
    )

    fun enableHistorySettings() {
        viewModelScope.launch {
            userPreferencesRepository.set(Keys.save_history, true)
        }
    }

    fun disableHistorySettings() {
        viewModelScope.launch {
            userPreferencesRepository.set(
                Keys.save_history to false,
                Keys.run_background_worker to false
            )
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            publicAddressRepository.deleteAll()
        }
    }
}
