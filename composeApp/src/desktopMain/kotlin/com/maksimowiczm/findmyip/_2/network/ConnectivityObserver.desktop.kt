@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.network

import com.maksimowiczm.findmyip._2.data.model.NetworkType

actual class ConnectivityObserver {
    // No way to get available network types on desktop for now
    actual val availableNetworkTypes = emptyList<NetworkType>()

    actual fun getNetworkType(): NetworkType? = null
}
