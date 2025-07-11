package com.maksimowiczm.findmyip.ext

import android.app.Notification
import androidx.core.app.NotificationManagerCompat

fun NotificationManagerCompat.notifyIfAllowed(id: Int, notification: Notification) {
    if (areNotificationsEnabled()) {
        notify(id, notification)
    }
}
