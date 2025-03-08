package com.maksimowiczm.findmyip

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.maksimowiczm.findmyip.data.initializer.AppInitializer
import com.maksimowiczm.findmyip.infrastructure.di.initKoin
import com.maksimowiczm.findmyip.ui.FindMyIpApp
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

fun main() = application {
    initKoin()

    val appInitializer = koinInject<AppInitializer>()
    LaunchedEffect(Unit) {
        appInitializer()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name)
    ) {
        FindMyIpApp()
    }
}
