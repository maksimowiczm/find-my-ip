package com.maksimowiczm.findmyip.presentation.currentaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIp4AddressUseCase
import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIp6AddressUseCase
import com.maksimowiczm.findmyip.application.usecase.RefreshIp4AddressUseCase
import com.maksimowiczm.findmyip.application.usecase.RefreshIp6AddressUseCase
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.IpAddress
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
    observeCurrentIp6AddressUseCase: ObserveCurrentIp6AddressUseCase,
    private val refreshIp6AddressUseCase: RefreshIp6AddressUseCase,
) : ViewModel() {

    private val ip4RefreshStatus = MutableStateFlow<RefreshStatus>(RefreshStatus.Idle)
    private val ip6RefreshStatus = MutableStateFlow<RefreshStatus>(RefreshStatus.Idle)

    private val ip4Flow =
        combine(ip4RefreshStatus, observeCurrentIp4AddressUseCase.observe()) {
            refreshStatus,
            addressStatus ->
            mapToUiState(refreshStatus, addressStatus)
        }

    private val ip6Flow =
        combine(ip6RefreshStatus, observeCurrentIp6AddressUseCase.observe()) {
            refreshStatus,
            addressStatus ->
            mapToUiState(refreshStatus, addressStatus)
        }

    val uiState =
        combine(ip4Flow, ip6Flow) { ip4State, ip6State ->
                CurrentAddressUiState(ip4 = ip4State, ip6 = ip6State)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MS),
                initialValue =
                    CurrentAddressUiState(
                        ip4 = IpAddressUiState.Loading(null, null),
                        ip6 = IpAddressUiState.Loading(null, null),
                    ),
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

        viewModelScope.launch {
            delay(REFRESH_DELAY_MS)

            refreshIp6AddressUseCase
                .refresh()
                .onSuccess { ip6RefreshStatus.value = RefreshStatus.Idle }
                .onError { error -> ip6RefreshStatus.value = RefreshStatus.Error(error.message) }
        }
    }

    private fun mapToUiState(
        refreshStatus: RefreshStatus,
        addressStatus: AddressStatus<IpAddress>,
    ): IpAddressUiState {
        val ipAddress =
            when (addressStatus) {
                is AddressStatus.Success -> addressStatus.value.stringRepresentation()
                is AddressStatus.Error -> null
            }

        return when (refreshStatus) {
            is RefreshStatus.Error -> IpAddressUiState.Error(ipAddress, addressStatus.date)

            RefreshStatus.Idle ->
                when (addressStatus) {
                    is AddressStatus.Error -> IpAddressUiState.Error(null, addressStatus.date)
                    is AddressStatus.Success ->
                        IpAddressUiState.Success(
                            addressStatus.value.stringRepresentation(),
                            addressStatus.date,
                        )
                }

            RefreshStatus.Refreshing ->
                when (addressStatus) {
                    is AddressStatus.Error -> IpAddressUiState.Error(null, addressStatus.date)
                    is AddressStatus.Success ->
                        IpAddressUiState.Loading(ipAddress, addressStatus.date)
                }
        }
    }

    companion object {
        private const val SUBSCRIPTION_TIMEOUT_MS = 2_000L
        private const val REFRESH_DELAY_MS = 1000L
    }
}
