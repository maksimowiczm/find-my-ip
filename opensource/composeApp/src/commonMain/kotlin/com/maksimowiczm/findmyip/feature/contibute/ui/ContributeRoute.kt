package com.maksimowiczm.findmyip.feature.contibute.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.maksimowiczm.findmyip.application.infrastructure.OpensourceAppConfig
import com.maksimowiczm.findmyip.shared.feature.contribute.ui.shareWithFriends
import org.koin.compose.koinInject

@Composable
fun ContributeRoute(onBack: () -> Unit, onSponsor: () -> Unit, modifier: Modifier = Modifier) {
    val appConfig: OpensourceAppConfig = koinInject()
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
