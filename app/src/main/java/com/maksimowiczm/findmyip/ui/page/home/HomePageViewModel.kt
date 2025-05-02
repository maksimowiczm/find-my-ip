package com.maksimowiczm.findmyip.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.AddressState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class HomePageViewModel(
    private val ipv4: AddressObserver,
    private val ipv6: AddressObserver,
    private val loadingDelayMillis: Long = 300
) : ViewModel() {

    val state = combine(ipv4.flow, ipv6.flow).runningFold(
        initial = HomePageState()
    ) { prev, (ipv4, ipv6) ->
        val ipv4state = toIpState(ipv4, prev.ipv4)
        val ipv6state = toIpState(ipv6, prev.ipv6)

        HomePageState(
            noInternetConnection = ipv4 is AddressState.Error && ipv6 is AddressState.Error,
            ipv4 = ipv4state,
            ipv6 = ipv6state
        )
    }.transform {
        emit(it)
        // Delay loading state to show loading animation
        if (it.isLoading && loadingDelayMillis > 0) {
            delay(loadingDelayMillis)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2_000),
        initialValue = HomePageState()
    )

    fun onRefresh() {
        viewModelScope.launch { ipv4.refresh() }
        viewModelScope.launch { ipv6.refresh() }
    }
}

private fun toIpState(new: AddressState, old: IpState): IpState = when (new) {
    is AddressState.Error -> IpState.NotDetected

    is AddressState.Refreshing -> when (old) {
        is IpState.Loading -> IpState.Loading(old.ip)
        IpState.NotDetected -> IpState.Loading(null)
        is IpState.Success -> IpState.Loading(old.ip)
    }

    is AddressState.Success -> IpState.Success(ip = new.address.ip)
}
