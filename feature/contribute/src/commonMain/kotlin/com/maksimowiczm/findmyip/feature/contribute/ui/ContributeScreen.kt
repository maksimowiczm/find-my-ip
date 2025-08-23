package com.maksimowiczm.findmyip.feature.contribute.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.shared.ui.ArrowBackIconButton
import com.maksimowiczm.findmyip.shared.ui.FindMyIpTheme
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val getAction: (ContributeAction) -> () -> Unit =
        remember(onSponsor, onShare, onFeatureRequest, onBugReport, onTranslate, onEmail) {
            {
                when (it) {
                    ContributeAction.Sponsor -> onSponsor
                    ContributeAction.ShareWithFriends -> onShare
                    ContributeAction.FeatureRequest -> onFeatureRequest
                    ContributeAction.BugReport -> onBugReport
                    ContributeAction.Translate -> onTranslate
                    ContributeAction.GetInTouch -> onEmail
                }
            }
        }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.headline_contribute)) },
                navigationIcon = { ArrowBackIconButton(onBack) },
                subtitle = { Text(stringResource(Res.string.description_contribute)) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    ),
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(360.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues.add(horizontal = 16.dp, vertical = 8.dp),
        ) {
            items(items = ContributeAction.entries) { action ->
                ContributeCard(
                    icon = {
                        Icon(action.icon, null, Modifier.size(ContributeCardDefaults.iconSize))
                    },
                    title = { Text(stringResource(action.title)) },
                    description = { Text(stringResource(action.description)) },
                    buttonLabel = { Text(stringResource(action.buttonLabel)) },
                    onAction = getAction(action),
                )
            }
        }
    }
}

private enum class ContributeAction(
    val icon: ImageVector,
    val title: StringResource,
    val description: StringResource,
    val buttonLabel: StringResource,
) {
    Sponsor(
        icon = Icons.Outlined.VolunteerActivism,
        title = Res.string.headline_become_a_sponsor,
        description = Res.string.description_become_a_sponsor,
        buttonLabel = Res.string.action_sponsor,
    ),
    ShareWithFriends(
        icon = Icons.Outlined.Share,
        title = Res.string.headline_share_with_friends,
        description = Res.string.description_share_with_friends,
        buttonLabel = Res.string.action_share,
    ),
    FeatureRequest(
        icon = Icons.Outlined.Lightbulb,
        title = Res.string.headline_feature_request,
        description = Res.string.description_feature_request,
        buttonLabel = Res.string.action_feature_request,
    ),
    BugReport(
        icon = Icons.Outlined.BugReport,
        title = Res.string.headline_bug_report,
        description = Res.string.description_bug_report,
        buttonLabel = Res.string.action_bug_report,
    ),
    Translate(
        icon = Icons.Outlined.Translate,
        title = Res.string.action_translate,
        description = Res.string.description_translate,
        buttonLabel = Res.string.action_translate,
    ),
    GetInTouch(
        icon = Icons.Outlined.Mail,
        title = Res.string.headline_get_in_touch,
        description = Res.string.description_get_in_touch,
        buttonLabel = Res.string.action_write_an_email,
    ),
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
