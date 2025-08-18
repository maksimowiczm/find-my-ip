package com.maksimowiczm.findmyip.presentation.currentaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIp4AddressUseCase
import com.maksimowiczm.findmyip.application.usecase.RefreshIp4AddressUseCase
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.shared.result.onError
import com.maksimowiczm.findmyip.shared.result.onSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CurrentAddressViewModel(
    observeCurrentIp4AddressUseCase: ObserveCurrentIp4AddressUseCase,
    private val refreshIp4AddressUseCase: RefreshIp4AddressUseCase,
) : ViewModel() {

    private val ip4RefreshStatus = MutableStateFlow<RefreshStatus>(RefreshStatus.Idle)

    val ip4Flow =
        combine(ip4RefreshStatus, observeCurrentIp4AddressUseCase.observe()) {
                refreshStatus,
                addressStatus ->
                mapToUiState(refreshStatus, addressStatus)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MS),
                initialValue = CurrentAddressUiState.Loading(null),
            )

    fun refresh() {
        ip4RefreshStatus.value = RefreshStatus.Refreshing

        viewModelScope.launch {
            delay(REFRESH_DELAY_MS)

            refreshIp4AddressUseCase
                .refresh()
                .onSuccess { ip4RefreshStatus.value = RefreshStatus.Idle }
                .onError { error -> ip4RefreshStatus.value = RefreshStatus.Error(error.message) }
        }
    }

    private fun mapToUiState(
        refreshStatus: RefreshStatus,
        addressStatus: AddressStatus<Ip4Address>,
    ): CurrentAddressUiState {
        val ipAddress =
            when (addressStatus) {
                is AddressStatus.Success -> addressStatus.value.stringRepresentation()
                is AddressStatus.Error -> null
            }

        return when (refreshStatus) {
            is RefreshStatus.Error -> CurrentAddressUiState.Error(ipAddress)

            RefreshStatus.Idle ->
                when (addressStatus) {
                    is AddressStatus.Error -> CurrentAddressUiState.Error(null)
                    is AddressStatus.Success ->
                        CurrentAddressUiState.Success(addressStatus.value.stringRepresentation())
                }

            RefreshStatus.Refreshing ->
                when (addressStatus) {
                    is AddressStatus.Error -> CurrentAddressUiState.Error(null)
                    is AddressStatus.Success -> CurrentAddressUiState.Loading(ipAddress)
                }
        }
    }

    companion object {
        private const val SUBSCRIPTION_TIMEOUT_MS = 2_000L
        private const val REFRESH_DELAY_MS = 200L
    }
}
