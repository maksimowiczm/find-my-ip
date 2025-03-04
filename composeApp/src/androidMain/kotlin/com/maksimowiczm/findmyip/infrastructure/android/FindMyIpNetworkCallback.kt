package com.maksimowiczm.findmyip.infrastructure.android

import android.net.ConnectivityManager
import android.net.Network
import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FindMyIpNetworkCallback(
    private val coroutineScope: CoroutineScope,
    private val addressRepository: AddressRepository
) : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        coroutineScope.launch {
            addressRepository.refreshAddressPersist(InternetProtocolVersion.IPv4)
            addressRepository.refreshAddressPersist(InternetProtocolVersion.IPv6)
        }
    }

    override fun onLost(network: Network) {
        coroutineScope.launch {
            addressRepository.refreshAddressPersist(InternetProtocolVersion.IPv4)
            addressRepository.refreshAddressPersist(InternetProtocolVersion.IPv6)
        }
    }
}
