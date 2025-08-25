package com.maksimowiczm.findmyip.feature.contibute.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.maksimowiczm.findmyip.shared.core.feature.ui.FindMyIpTheme
import com.maksimowiczm.findmyip.shared.feature.contribute.ui.ContributeAction
import com.maksimowiczm.findmyip.shared.feature.contribute.ui.ContributeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ContributeScreen(
    onBack: () -> Unit,
    onSponsor: () -> Unit,
    onShare: () -> Unit,
    onFeatureRequest: () -> Unit,
    onBugReport: () -> Unit,
    onTranslate: () -> Unit,
    onEmail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val actions =
        listOf(
            ContributeAction.sponsor(onSponsor),
            ContributeAction.shareWithFriends(onShare),
            ContributeAction.featureRequest(onFeatureRequest),
            ContributeAction.bugReport(onBugReport),
            ContributeAction.translate(onTranslate),
            ContributeAction.getInTouch(onEmail),
        )

    ContributeScreen(onBack = onBack, actions = actions, modifier = modifier)
}

@Preview
@Composable
private fun ContributeScreenPreview() {
    FindMyIpTheme {
        ContributeScreen(
            onBack = {},
            onSponsor = {},
            onShare = {},
            onFeatureRequest = {},
            onBugReport = {},
            onTranslate = {},
            onEmail = {},
        )
    }
}
