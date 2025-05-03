package com.maksimowiczm.findmyip.infrastructure.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.domain.model.Address
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.domain.notification.AddressNotificationService

class AndroidNotificationService(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)
) : AddressNotificationService {

    init {
        createNewAddressNotificationChannel(context)
    }

    override fun postAddress(address: Address) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_ip_change) // TODO
            .setContentTitle(context.getString(R.string.headline_new_address_detected))
            .setContentText(address.notificationText())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notifyIfAllowed(
            id = address.id.value.toInt(),
            notification = notification
        )
    }

    private fun Address.notificationText() = buildString {
        append(ip)
        append(", ")
        append(networkType.string())
    }

    private fun NetworkType.string() = when (this) {
        NetworkType.Cellular -> context.getString(R.string.cellular)
        NetworkType.VPN -> context.getString(R.string.vpn)
        NetworkType.WiFi -> context.getString(R.string.wifi)
    }
}

private const val CHANNEL_ID = "address_notification_channel"

private fun createNewAddressNotificationChannel(context: Context) {
    val name = context.getString(R.string.headline_ip_change)
    val descriptionText = context.getString(R.string.description_notifications)
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
        description = descriptionText
    }

    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.createNotificationChannel(channel)
}

private fun NotificationManagerCompat.notifyIfAllowed(id: Int, notification: Notification) {
    if (areNotificationsEnabled()) {
        notify(id, notification)
    }
}
