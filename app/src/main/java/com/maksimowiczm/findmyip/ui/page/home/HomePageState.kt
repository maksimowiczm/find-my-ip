package com.maksimowiczm.findmyip.ui.page.home

data class HomePageState(
    val noInternetConnection: Boolean = false,
    val ipv4: IpState = IpState.Loading(null),
    val ipv6: IpState = IpState.Loading(null)
)

sealed interface IpState {
    data class Loading(val ip: String?) : IpState
    data object NotDetected : IpState
    data class Success(val ip: String) : IpState
}
