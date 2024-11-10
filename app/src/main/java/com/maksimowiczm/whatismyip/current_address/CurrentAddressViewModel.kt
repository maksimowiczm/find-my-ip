package com.maksimowiczm.whatismyip.current_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.mapBoth
import com.maksimowiczm.whatismyip.domain.ObserveCurrentAddressUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CurrentAddressViewModel(
    observeCurrentAddressUseCase: ObserveCurrentAddressUseCase
) : ViewModel() {
    val currentAddressUiState =
        observeCurrentAddressUseCase()
            .map { result ->
                result.mapBoth(
                    success = { CurrentAddressUiState.Success(it) },
                    failure = { CurrentAddressUiState.Error }
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(2000),
                initialValue = CurrentAddressUiState.Loading
            )

    fun refreshCurrentAddress() {}
}

sealed interface CurrentAddressUiState {
    object Loading : CurrentAddressUiState
    data class Success(val address: String) : CurrentAddressUiState
    data object Error : CurrentAddressUiState
}
