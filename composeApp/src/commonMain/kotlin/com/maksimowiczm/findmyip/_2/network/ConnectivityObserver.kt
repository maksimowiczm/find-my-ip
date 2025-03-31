@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.network

import com.maksimowiczm.findmyip._2.data.model.NetworkType

expect class ConnectivityObserver {
    val availableNetworkTypes: List<NetworkType>

    fun getNetworkType(): NetworkType?
}
