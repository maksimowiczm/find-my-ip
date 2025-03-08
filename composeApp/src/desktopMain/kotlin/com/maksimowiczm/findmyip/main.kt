package com.maksimowiczm.findmyip

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.maksimowiczm.findmyip.ui.FindMyIpApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject",
    ) {
        FindMyIpApp()
    }
}