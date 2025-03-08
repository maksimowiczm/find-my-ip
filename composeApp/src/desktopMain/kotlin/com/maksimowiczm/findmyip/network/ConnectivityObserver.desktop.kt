package com.maksimowiczm.findmyip.network

import com.maksimowiczm.findmyip.data.model.NetworkType

actual class ConnectivityObserver {
    actual fun getNetworkType(): NetworkType? = NetworkType.WIFI
}
