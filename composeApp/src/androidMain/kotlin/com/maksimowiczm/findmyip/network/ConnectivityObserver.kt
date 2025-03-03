package com.maksimowiczm.findmyip.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_VPN
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import com.maksimowiczm.findmyip.data.model.NetworkType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

actual class ConnectivityObserver(private val context: Context) {
    fun getNetworkType(): NetworkType? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.toNetworkType()
    }

    /**
     * Observes network connection changes.
     * Emits the current network type and then emits new network type when it changes.
     */
    actual fun observeNetworkType(): Flow<NetworkType?> = callbackFlow {
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

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}

private fun NetworkCapabilities.toNetworkType(): NetworkType? = when {
    // Check for VPN first, as it is a subset of mobile capabilities
    hasTransport(TRANSPORT_VPN) -> NetworkType.VPN
    hasTransport(TRANSPORT_WIFI) -> NetworkType.WIFI
    hasTransport(TRANSPORT_CELLULAR) -> NetworkType.MOBILE
    else -> null
}
