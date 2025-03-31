package com.maksimowiczm.findmyip

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.maksimowiczm.findmyip.infrastructure.di.startKoin
import com.maksimowiczm.findmyip.ui.FindMyIpApp

fun main() = application {
    startKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Find My IP"
    ) {
        FindMyIpApp()
    }
}
