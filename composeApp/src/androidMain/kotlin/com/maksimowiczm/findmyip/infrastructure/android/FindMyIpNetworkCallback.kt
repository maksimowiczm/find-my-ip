package com.maksimowiczm.findmyip.infrastructure.android

import android.net.ConnectivityManager
import android.net.Network
import com.maksimowiczm.findmyip.data.AddressRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FindMyIpNetworkCallback(
    private val coroutineScope: CoroutineScope,
    private val addressRepository: AddressRepository
) : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        coroutineScope.launch {
            addressRepository.refreshAddresses()
        }
    }

    override fun onLost(network: Network) {
        coroutineScope.launch {
            addressRepository.refreshAddresses()
        }
    }
}
