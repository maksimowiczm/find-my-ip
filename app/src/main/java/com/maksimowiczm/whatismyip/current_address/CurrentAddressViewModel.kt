package com.maksimowiczm.whatismyip.current_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.mapBoth
import com.maksimowiczm.whatismyip.data.repository.PublicAddressRepository
import com.maksimowiczm.whatismyip.domain.ObserveCurrentAddressUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CurrentAddressViewModel(
    private val publicAddressRepository: PublicAddressRepository,
    observeCurrentAddressUseCase: ObserveCurrentAddressUseCase
) : ViewModel() {
    private val _shouldRefreshAddress = MutableStateFlow(false)

    val currentAddressUiState = combine(
        observeCurrentAddressUseCase(),
        _shouldRefreshAddress
    ) { result, isRefreshing ->
        if (isRefreshing) {
            return@combine CurrentAddressUiState.Loading
        }

        result.mapBoth(
            success = { CurrentAddressUiState.Success(it) },
            failure = { CurrentAddressUiState.Error }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = CurrentAddressUiState.Loading
    )


    fun refreshCurrentAddress() {
        viewModelScope.launch {
            // Make spinner visible
            _shouldRefreshAddress.emit(true)
            delay(200)

            publicAddressRepository.refreshCurrentAddress()
            _shouldRefreshAddress.emit(false)
        }
    }
}

sealed interface CurrentAddressUiState {
    object Loading : CurrentAddressUiState
    data class Success(val address: String) : CurrentAddressUiState
    data object Error : CurrentAddressUiState
}
