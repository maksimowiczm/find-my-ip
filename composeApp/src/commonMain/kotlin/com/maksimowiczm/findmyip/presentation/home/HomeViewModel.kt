package com.maksimowiczm.findmyip.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maksimowiczm.findmyip.application.usecase.ObserveAddressHistoryUseCase
import com.maksimowiczm.findmyip.application.usecase.RefreshIp4AddressUseCase
import com.maksimowiczm.findmyip.application.usecase.RefreshIp6AddressUseCase
import com.maksimowiczm.findmyip.shared.result.isError
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    observeHistoryUseCase: ObserveAddressHistoryUseCase,
    private val refreshIp4AddressUseCase: RefreshIp4AddressUseCase,
    private val refreshIp6AddressUseCase: RefreshIp6AddressUseCase,
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    val history = observeHistoryUseCase.observe().cachedIn(viewModelScope)

    fun refresh() {
        if (_isRefreshing.value) return

        _isRefreshing.value = true
        _isError.value = false

        val ip4Job =
            viewModelScope.async {
                delay(REFRESH_DELAY_MS)
                refreshIp4AddressUseCase.refresh()
            }
        val ip6Job =
            viewModelScope.async {
                delay(REFRESH_DELAY_MS)
                refreshIp6AddressUseCase.refresh()
            }

        viewModelScope.launch {
            val ip4 = ip4Job.await()
            val ip6 = ip6Job.await()
            _isRefreshing.value = false

            // If both are errors, set error state
            if (ip4.isError() && ip6.isError()) {
                _isError.value = true
            }
        }
    }

    companion object {
        private const val REFRESH_DELAY_MS = 1000L
    }
}
