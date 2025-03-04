package com.maksimowiczm.findmyip.network

import com.maksimowiczm.findmyip.data.model.NetworkType

expect class ConnectivityObserver {
    fun getNetworkType(): NetworkType?
}
