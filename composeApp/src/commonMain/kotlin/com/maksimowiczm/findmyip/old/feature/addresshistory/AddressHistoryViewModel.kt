package com.maksimowiczm.findmyip.old.feature.addresshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.old.data.Keys
import com.maksimowiczm.findmyip.old.data.UserPreferencesRepository
import com.maksimowiczm.findmyip.old.domain.AddressHistory
import com.maksimowiczm.findmyip.old.domain.ObserveAddressHistoryUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AddressHistoryViewModel(
    userPreferencesRepository: UserPreferencesRepository,
    observeAddressHistoryUseCase: ObserveAddressHistoryUseCase
) : ViewModel() {
    val hasPermission = userPreferencesRepository.observe(Keys.save_history)
        .map { it == true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = false
        )

    val ipv4HistoryState = combine(
        observeAddressHistoryUseCase(InternetProtocolVersion.IPv4),
        userPreferencesRepository.observe(Keys.ipv4_enabled)
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
        userPreferencesRepository.observe(Keys.ipv6_enabled)
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
}

sealed interface AddressHistoryState {
    data object Loading : AddressHistoryState
    data object Disabled : AddressHistoryState
    data class Loaded(val addressHistory: List<AddressHistory>) : AddressHistoryState
}
