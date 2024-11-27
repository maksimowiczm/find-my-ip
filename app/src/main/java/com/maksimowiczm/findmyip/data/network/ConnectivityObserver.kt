package com.maksimowiczm.findmyip.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
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

            return when {
                capabilities == null -> null
                capabilities.hasTransport(TRANSPORT_WIFI) -> NetworkType.WIFI
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> NetworkType.MOBILE
                capabilities.hasTransport(TRANSPORT_VPN) -> NetworkType.VPN
                else -> null
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo

            @Suppress("DEPRECATION")
            return when {
                networkInfo == null -> null
                networkInfo.type == ConnectivityManager.TYPE_WIFI -> NetworkType.WIFI
                networkInfo.type == ConnectivityManager.TYPE_MOBILE -> NetworkType.MOBILE
                networkInfo.type == ConnectivityManager.TYPE_VPN -> NetworkType.VPN
                else -> null
            }
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

                when {
                    capabilities.hasTransport(TRANSPORT_WIFI) -> trySend(NetworkType.WIFI)
                    capabilities.hasTransport(TRANSPORT_CELLULAR) -> trySend(NetworkType.MOBILE)
                    capabilities.hasTransport(TRANSPORT_VPN) -> trySend(NetworkType.VPN)
                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                when {
                    networkCapabilities.hasTransport(TRANSPORT_WIFI) -> trySend(NetworkType.WIFI)
                    networkCapabilities.hasTransport(
                        TRANSPORT_CELLULAR
                    ) -> trySend(NetworkType.MOBILE)

                    networkCapabilities.hasTransport(TRANSPORT_VPN) -> trySend(NetworkType.VPN)
                }
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
