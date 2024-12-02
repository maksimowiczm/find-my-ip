package com.maksimowiczm.findmyip.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.net.NetworkInfo
import android.os.Build
import com.maksimowiczm.findmyip.data.model.NetworkType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class ConnectivityObserver(
    private val context: Context
) {
    fun getNetworkType(): NetworkType? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities?.toNetworkType()
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.toNetworkType()
        }
    }

    /**
     * Observes network connection changes.
     * Emits the current network type and then emits new network type when it changes.
     */
    fun observeConnection(): Flow<NetworkType?> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val capabilities = connectivityManager.getNetworkCapabilities(network)

                if (capabilities == null) {
                    return
                }

                val networkType = capabilities.toNetworkType()
                trySend(networkType)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val networkType = networkCapabilities.toNetworkType()
                trySend(networkType)
            }

            override fun onLost(network: Network) {
                trySend(null)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(callback)
        } else {
            val networkRequest = android.net.NetworkRequest.Builder()
                .addTransportType(TRANSPORT_WIFI)
                .addTransportType(TRANSPORT_CELLULAR)
                .addTransportType(TRANSPORT_VPN)
                .build()

            connectivityManager.registerNetworkCallback(networkRequest, callback)
        }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}

private fun NetworkCapabilities.toNetworkType(): NetworkType? {
    return when {
        // Check for VPN first, as it is a subset of mobile capabilities
        hasTransport(TRANSPORT_VPN) -> NetworkType.VPN
        hasTransport(TRANSPORT_WIFI) -> NetworkType.WIFI
        hasTransport(TRANSPORT_CELLULAR) -> NetworkType.MOBILE
        else -> null
    }
}

@Suppress("DEPRECATION")
private fun NetworkInfo.toNetworkType(): NetworkType? {
    return when {
        type == ConnectivityManager.TYPE_VPN -> NetworkType.VPN
        type == ConnectivityManager.TYPE_WIFI -> NetworkType.WIFI
        type == ConnectivityManager.TYPE_MOBILE -> NetworkType.MOBILE
        else -> null
    }
}
