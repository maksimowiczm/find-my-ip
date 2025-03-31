@file:Suppress("PackageName")

package com.maksimowiczm.findmyip

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.maksimowiczm.findmyip._2.data.HistoryManager
import com.maksimowiczm.findmyip._2.data.initializer.AppInitializer
import com.maksimowiczm.findmyip._2.infrastructure.di.initKoin
import com.maksimowiczm.findmyip._2.ui.FindMyIpApp
import findmyip.composeapp.generated.resources.*
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

fun main() = application {
    initKoin()

    val appInitializer = koinInject<AppInitializer>()
    LaunchedEffect(Unit) {
        appInitializer()
    }
    val historyManager = koinInject<HistoryManager>()
    LaunchedEffect(Unit) {
        historyManager.run()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        icon = painterResource(resource = Res.drawable.ic_launcher)
    ) {
        FindMyIpApp()
    }
}
