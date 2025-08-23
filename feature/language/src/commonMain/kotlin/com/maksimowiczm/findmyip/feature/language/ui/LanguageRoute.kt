package com.maksimowiczm.findmyip.feature.language.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.maksimowiczm.findmyip.application.infrastructure.config.AppConfig
import org.koin.compose.koinInject

@Composable
fun LanguageRoute(onBack: () -> Unit, modifier: Modifier = Modifier.Companion) {
    val appConfig: AppConfig = koinInject()
    val uriHandler = LocalUriHandler.current

    LanguageScreen(
        onBack = onBack,
        onTranslate = { uriHandler.openUri(appConfig.translateUrl) },
        modifier = modifier,
    )
}
