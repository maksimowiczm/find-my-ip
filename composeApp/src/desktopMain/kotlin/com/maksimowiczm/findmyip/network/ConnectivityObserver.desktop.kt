package com.maksimowiczm.findmyip.network

import com.maksimowiczm.findmyip.data.model.NetworkType

actual class ConnectivityObserver {
    // No way to get available network types on desktop for now
    actual val availableNetworkTypes = emptyList<NetworkType>()

    actual fun getNetworkType(): NetworkType? = null
}
