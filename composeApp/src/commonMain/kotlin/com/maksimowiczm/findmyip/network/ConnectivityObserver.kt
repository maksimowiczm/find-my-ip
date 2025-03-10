package com.maksimowiczm.findmyip.network

import com.maksimowiczm.findmyip.data.model.NetworkType

expect class ConnectivityObserver {
    val availableNetworkTypes: List<NetworkType>

    fun getNetworkType(): NetworkType?
}
