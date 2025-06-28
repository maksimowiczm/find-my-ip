package com.maksimowiczm.findmyip.infrastructure.android

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.domain.backgroundservices.ForegroundService
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.AddressState
import com.maksimowiczm.findmyip.domain.usecase.ObserveCurrentAddressUseCase
import com.maksimowiczm.findmyip.ext.notifyIfAllowed
import kotlin.apply
import kotlin.jvm.java
import kotlin.onFailure
import kotlin.run
import kotlin.runCatching
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class AndroidForegroundService(private val context: Context) : ForegroundService {

    // Poll service state
    override val isRunning: Flow<Boolean>
        get() = flow {
            while (true) {
                Log.d(TAG, "Polling service state")
                emit(NetworkMonitoringService.isRunning())
                delay(500)
            }
        }.onCompletion {
            Log.d(TAG, "Polling service state completed")
        }

    private val intent: Intent
        get() = Intent(context, NetworkMonitoringService::class.java)

    override suspend fun start() {
        context.startForegroundService(intent)
    }

    override suspend fun stop() {
        context.stopService(intent)
    }

    private companion object {
        const val TAG = "AndroidForegroundService"
    }
}

class NetworkMonitoringService : Service() {

    private val notificationManager: NotificationManagerCompat
        get() = NotificationManagerCompat.from(this)

    private val connectivityManager: ConnectivityManager
        get() = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val observeIpv4 by inject<ObserveCurrentAddressUseCase>(named(InternetProtocol.IPv4))
    private val refreshIpv4 by inject<AddressObserver>(named(InternetProtocol.IPv4))
    private val observeIpv6 by inject<ObserveCurrentAddressUseCase>(named(InternetProtocol.IPv6))
    private val refreshIpv6 by inject<AddressObserver>(named(InternetProtocol.IPv6))

    private val networkCallback = MyNetworkCallback {
        Log.d(TAG, "Network capabilities changed")
        coroutineScope.launch { refreshIpv4.refresh() }
        coroutineScope.launch { refreshIpv6.refresh() }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
        coroutineScope.cancel()
        super.onDestroy()
        runBlocking { mutex.withLock { isRunning = false } }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()

        combine(
            observeIpv4.observe(),
            observeIpv6.observe()
        ) { ipv4, ipv6 ->
            notificationManager.notifyIfAllowed(
                id = NOTIFICATION_ID,
                notification = buildNotification(ipv4, ipv6)
            )
        }.launchIn(coroutineScope)

        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        coroutineScope.launch {
            mutex.withLock { isRunning = true }
        }

        return START_STICKY
    }

    private fun startForeground() = runCatching {
        val channelName = getString(R.string.notification_network_monitor)

        val channel = NotificationChannel(
            CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)

        val serviceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
        } else {
            0
        }

        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            buildNotification(null, null),
            serviceType
        )
    }.onFailure { e ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            e is ForegroundServiceStartNotAllowedException
        ) {
            Log.e(TAG, "Foreground service start not allowed", e)
        }
    }

    private fun buildNotification(ipv4: AddressState?, ipv6: AddressState?): Notification {
        val intent =
            Intent(Intent.ACTION_VIEW, "findmyip://home".toUri(), this, MainActivity::class.java)

        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val title = getString(R.string.notification_network_monitor)
        val enDash = getString(R.string.en_dash)

        return NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle(title)
            setSmallIcon(R.drawable.ic_notification_ip_change)
            setOngoing(true)
            setContentIntent(pendingIntent)

            if (ipv4 != null && ipv6 != null) {
                val contentText = buildString {
                    append(this@NetworkMonitoringService.getString(R.string.ipv4))
                    append(" $enDash ")
                    append(ipv4.notificationText())
                    append(",\n")
                    append(this@NetworkMonitoringService.getString(R.string.ipv6))
                    append(" $enDash ")
                    append(ipv6.notificationText())
                }

                setContentText(contentText)
            }
        }.build()
    }

    private fun AddressState.notificationText() = when (this) {
        is AddressState.Error -> getString(R.string.neutral_not_detected)
        AddressState.Refreshing -> getString(R.string.neutral_refreshing)
        is AddressState.Success -> buildString {
            append(address.ip)
            append(", ")
            append(address.networkType.string())
        }
    }

    private fun NetworkType.string() = when (this) {
        NetworkType.Cellular -> getString(R.string.cellular)
        NetworkType.VPN -> getString(R.string.vpn)
        NetworkType.WiFi -> getString(R.string.wifi)
    }

    companion object {

        // This is bad :) idea, because onDestroy may never run
        private var isRunning = false
        private val mutex = Mutex()
        suspend fun isRunning(): Boolean = mutex.withLock { isRunning }

        private const val TAG = "NetworkMonitoringService"
        private const val CHANNEL_ID = "NETWORK_MONITORING_CHANNEL"
        private const val NOTIFICATION_ID = 100
    }
}

private class MyNetworkCallback(private val callback: () -> Unit) :
    ConnectivityManager.NetworkCallback() {
    private var lastNetworkTransportType: Int? = null

    override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
        val transportType = when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                NetworkCapabilities.TRANSPORT_WIFI

            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                NetworkCapabilities.TRANSPORT_CELLULAR

            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ->
                NetworkCapabilities.TRANSPORT_VPN

            else -> null
        }

        if (transportType != null && transportType != lastNetworkTransportType) {
            lastNetworkTransportType = transportType
            callback()
        }
    }

    override fun onLost(network: Network) {
        lastNetworkTransportType = null
        callback()
    }
}
