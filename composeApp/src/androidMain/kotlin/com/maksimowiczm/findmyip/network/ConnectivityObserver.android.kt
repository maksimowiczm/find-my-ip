package com.maksimowiczm.findmyip.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_VPN
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import com.maksimowiczm.findmyip.data.model.NetworkType

actual class ConnectivityObserver(private val context: Context) {
    actual suspend fun getNetworkType(): NetworkType {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.toNetworkType() ?: NetworkType.UNKNOWN
    }
}

private fun NetworkCapabilities.toNetworkType(): NetworkType? = when {
    // Check for VPN first, as it is a subset of mobile capabilities
    hasTransport(TRANSPORT_VPN) -> NetworkType.VPN
    hasTransport(TRANSPORT_WIFI) -> NetworkType.WIFI
    hasTransport(TRANSPORT_CELLULAR) -> NetworkType.MOBILE
    else -> null
}
