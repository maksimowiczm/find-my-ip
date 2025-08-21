package com.maksimowiczm.findmyip.ui.contribute

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.maksimowiczm.findmyip.application.infrastructure.config.AppConfig
import org.koin.compose.koinInject

@Composable
fun ContributeRoute(onBack: () -> Unit, onSponsor: () -> Unit, modifier: Modifier = Modifier) {
    val appConfig: AppConfig = koinInject()
    val uriHandler = LocalUriHandler.current

    ContributeScreen(
        onBack = onBack,
        modifier = modifier,
        onSponsor = onSponsor,
        onShare = shareWithFriends(),
        onFeatureRequest = { uriHandler.openUri(appConfig.featureRequestUrl) },
        onBugReport = { uriHandler.openUri(appConfig.bugReportUrl) },
        onTranslate = { uriHandler.openUri(appConfig.translateUrl) },
        onEmail = { uriHandler.openUri(appConfig.feedbackEmailUri) },
    )
}

@Composable internal expect fun shareWithFriends(): () -> Unit
