package com.maksimowiczm.findmyip.feature.contribute.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.maksimowiczm.findmyip.application.infrastructure.config.AppConfig
import com.maksimowiczm.findmyip.feature.contribute.common.ui.shareWithFriends
import org.koin.compose.koinInject

@Composable
fun ContributeRoute(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val appConfig: AppConfig = koinInject()
    val uriHandler = LocalUriHandler.current

    ContributeScreen(
        onBack = onBack,
        modifier = modifier,
        onShare = shareWithFriends(),
        onTranslate = { uriHandler.openUri(appConfig.translateUrl) },
        onEmail = { uriHandler.openUri(appConfig.feedbackEmailUri) },
    )
}
