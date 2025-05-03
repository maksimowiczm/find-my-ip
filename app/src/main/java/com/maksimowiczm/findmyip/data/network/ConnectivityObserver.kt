package com.maksimowiczm.findmyip.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_VPN
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import com.maksimowiczm.findmyip.domain.model.NetworkType

fun interface ConnectivityObserver {
    fun getNetworkType(): NetworkType?
}

class AndroidConnectivityObserver(private val context: Context) : ConnectivityObserver {
    val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun getNetworkType(): NetworkType? {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.toNetworkType()
    }
}

private fun NetworkCapabilities.toNetworkType(): NetworkType? = when {
    // Check for VPN first, as it is a subset of mobile capabilities
    hasTransport(TRANSPORT_VPN) -> NetworkType.VPN
    hasTransport(TRANSPORT_WIFI) -> NetworkType.WiFi
    hasTransport(TRANSPORT_CELLULAR) -> NetworkType.Cellular
    else -> null
}
