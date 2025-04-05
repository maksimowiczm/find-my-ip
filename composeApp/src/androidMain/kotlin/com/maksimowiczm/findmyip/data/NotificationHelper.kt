package com.maksimowiczm.findmyip.data

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.infrastructure.android.MainActivity
import findmyip.composeapp.generated.resources.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString

class NotificationHelper(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat
) {
    fun notifyAddressChange(address: Address) {
        val title = runBlocking { getString(Res.string.notification_title_address_changed) }
        val content = address.ip

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        notifyIfAllowed(
            id = when (address.protocol) {
                InternetProtocolVersion.IPv4 -> IPV4_CHANNEL_ID
                InternetProtocolVersion.IPv6 -> IPV6_CHANNEL_ID
            },
            notification = builder.build()
        )
    }

    fun notifyIfAllowed(id: Int, notification: Notification) {
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(id, notification)
        }
    }

    companion object {
        const val CHANNEL_ID = "com.maksimowiczm.findmyip"
        const val IPV4_CHANNEL_ID = 10
        const val IPV6_CHANNEL_ID = 11

        // TODO localize name
        fun initNotificationChannel(context: Context) {
            val channelName =
                runBlocking { getString(Res.string.notification_channel_name_address_changed) }

            val notificationChannel =
                NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
            val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
    }
}
