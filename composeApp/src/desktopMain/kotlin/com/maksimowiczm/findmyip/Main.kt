package com.maksimowiczm.findmyip

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.maksimowiczm.findmyip.infrastructure.di.initKoin
import com.maksimowiczm.findmyip.ui.FindMyIpApp
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name)
    ) {
        FindMyIpApp()
    }
}
