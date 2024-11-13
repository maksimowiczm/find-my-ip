package com.maksimowiczm.whatismyip.currentaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.mapBoth
import com.maksimowiczm.whatismyip.data.model.Address
import com.maksimowiczm.whatismyip.data.repository.PublicAddressRepository
import com.maksimowiczm.whatismyip.domain.ObserveCurrentAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CurrentAddressViewModel
@Inject
constructor(
    private val publicAddressRepository: PublicAddressRepository,
    observeCurrentAddressUseCase: ObserveCurrentAddressUseCase
) : ViewModel() {
    private val shouldRefreshAddress = MutableStateFlow(false)

    val currentAddressUiState =
        combine(
            observeCurrentAddressUseCase(),
            shouldRefreshAddress
        ) { result, isRefreshing ->
            if (isRefreshing) {
                return@combine CurrentAddressUiState.Loading
            }

            result.mapBoth(
                success = {
                    when (it) {
                        is Address -> return@combine CurrentAddressUiState.Success(it.ip)
                        null -> return@combine CurrentAddressUiState.Loading
                    }
                },
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
            shouldRefreshAddress.emit(true)
            delay(200)

            publicAddressRepository.refreshCurrentAddress()
            shouldRefreshAddress.emit(false)
        }
    }
}

sealed interface CurrentAddressUiState {
    object Loading : CurrentAddressUiState

    data class Success(val address: String) : CurrentAddressUiState

    data object Error : CurrentAddressUiState
}
