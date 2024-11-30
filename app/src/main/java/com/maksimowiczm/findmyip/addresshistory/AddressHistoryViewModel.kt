package com.maksimowiczm.findmyip.addresshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.Keys
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import com.maksimowiczm.findmyip.domain.AddressHistory
import com.maksimowiczm.findmyip.domain.ObserveAddressHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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

    val ipv4HistoryState = combine(
        observeAddressHistoryUseCase(InternetProtocolVersion.IPv4),
        userPreferencesRepository.get(Keys.ipv4_enabled)
    ) { history, enabled ->
        if (enabled != true) {
            return@combine AddressHistoryState.Disabled
        }

        AddressHistoryState.Loaded(history)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = AddressHistoryState.Loading
    )

    val ipv6HistoryState = combine(
        observeAddressHistoryUseCase(InternetProtocolVersion.IPv6),
        userPreferencesRepository.get(Keys.ipv6_enabled)
    ) { history, enabled ->
        if (enabled != true) {
            return@combine AddressHistoryState.Disabled
        }

        AddressHistoryState.Loaded(history)
    }.stateIn(
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
    data object Disabled : AddressHistoryState
    data class Loaded(
        val addressHistory: List<AddressHistory>
    ) : AddressHistoryState
}
