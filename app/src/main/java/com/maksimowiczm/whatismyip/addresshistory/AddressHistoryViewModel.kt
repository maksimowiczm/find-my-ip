package com.maksimowiczm.whatismyip.addresshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.whatismyip.data.Keys
import com.maksimowiczm.whatismyip.data.repository.UserPreferencesRepository
import com.maksimowiczm.whatismyip.domain.AddressHistory
import com.maksimowiczm.whatismyip.domain.ObserveAddressHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AddressHistoryViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    observeAddressHistoryUseCase: ObserveAddressHistoryUseCase
) : ViewModel() {
    val hasPermission = userPreferencesRepository.get(Keys.save_history)
        .map { it == true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = false
        )

    val addressHistoryState = observeAddressHistoryUseCase()
        .map { AddressHistoryState.Loaded(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = AddressHistoryState.Loading
        )

    fun onGrantPermission() {
        viewModelScope.launch {
            userPreferencesRepository.set(Keys.save_history, true)
        }
    }
}

sealed interface AddressHistoryState {
    data object Loading : AddressHistoryState
    data class Loaded(val addressHistory: List<AddressHistory>) : AddressHistoryState
}
