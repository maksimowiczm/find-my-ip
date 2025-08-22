package com.maksimowiczm.findmyip.application.infrastructure.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_VPN
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import com.maksimowiczm.findmyip.domain.entity.NetworkType

internal class AndroidNetworkTypeObserver(private val context: Context) : NetworkTypeObserver {

    val connectivityManager: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun getNetworkType(): NetworkType = connectivityManager.getNetworkType()

    private fun ConnectivityManager.getNetworkType() =
        getNetworkCapabilities(activeNetwork).toNetworkType()
}

private fun NetworkCapabilities?.toNetworkType(): NetworkType =
    when {
        null == this -> NetworkType.Unknown
        // Check for VPN first, as it is a subset of mobile capabilities
        hasTransport(TRANSPORT_VPN) -> NetworkType.VPN
        hasTransport(TRANSPORT_WIFI) -> NetworkType.WiFi
        hasTransport(TRANSPORT_CELLULAR) -> NetworkType.Cellular
        else -> NetworkType.Unknown
    }
