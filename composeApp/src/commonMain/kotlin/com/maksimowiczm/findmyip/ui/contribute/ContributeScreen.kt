package com.maksimowiczm.findmyip.ui.contribute

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.ui.shared.ArrowBackIconButton
import com.maksimowiczm.findmyip.ui.shared.FindMyIpTheme
import com.maksimowiczm.findmyip.ui.shared.SettingsListItem
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContributeScreen(
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

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = { ArrowBackIconButton(onBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { SupportWithMoney(onSponsor = onSponsor) }
            item { SpreadTheWord(onShare = onShare) }
            item {
                GetInvolved(
                    onFeatureRequest = onFeatureRequest,
                    onBugReport = onBugReport,
                    onTranslate = onTranslate,
                    onEmail = onEmail,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SupportWithMoney(onSponsor: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = stringResource(Res.string.headline_support_with_money),
            style = MaterialTheme.typography.headlineMediumEmphasized,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        SettingsListItem(
            icon = { Icon(Icons.Outlined.VolunteerActivism, null) },
            title = { Text(stringResource(Res.string.headline_sponsor)) },
            onClick = onSponsor,
            shape = MaterialTheme.shapes.large,
            supportingText = { Text(stringResource(Res.string.description_sponsor)) },
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SpreadTheWord(onShare: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = stringResource(Res.string.headline_spread_the_word),
            style = MaterialTheme.typography.headlineMediumEmphasized,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        SettingsListItem(
            icon = { Icon(Icons.Outlined.Share, null) },
            title = { Text(stringResource(Res.string.action_share_with_friends)) },
            onClick = onShare,
            shape = MaterialTheme.shapes.large,
            supportingText = { Text(stringResource(Res.string.description_share_with_friends)) },
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun GetInvolved(
    onFeatureRequest: () -> Unit,
    onBugReport: () -> Unit,
    onTranslate: () -> Unit,
    onEmail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = stringResource(Res.string.headline_get_involved),
            style = MaterialTheme.typography.headlineMediumEmphasized,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        SettingsListItem(
            icon = { Icon(Icons.Outlined.Lightbulb, null) },
            title = { Text(stringResource(Res.string.action_feature_request_on_github)) },
            onClick = onFeatureRequest,
            shape =
                RoundedCornerShape(
                    topStart = MaterialTheme.shapes.large.topStart,
                    topEnd = MaterialTheme.shapes.large.topEnd,
                    bottomStart = MaterialTheme.shapes.extraSmall.bottomStart,
                    bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd,
                ),
            supportingText = {
                Text(stringResource(Res.string.description_feature_request_on_github))
            },
        )
        Spacer(Modifier.height(2.dp))
        SettingsListItem(
            icon = { Icon(Icons.Outlined.BugReport, null) },
            title = { Text(stringResource(Res.string.action_report_issue_on_github)) },
            onClick = onBugReport,
            supportingText = { Text(stringResource(Res.string.description_report_issue_on_github)) },
        )
        Spacer(Modifier.height(2.dp))
        SettingsListItem(
            icon = { Icon(Icons.Outlined.Translate, null) },
            title = { Text(stringResource(Res.string.action_translate)) },
            onClick = onTranslate,
            supportingText = { Text(stringResource(Res.string.description_translate)) },
        )
        Spacer(Modifier.height(2.dp))
        SettingsListItem(
            icon = { Icon(Icons.Outlined.Mail, null) },
            title = { Text(stringResource(Res.string.action_write_an_email)) },
            onClick = onEmail,
            shape =
                RoundedCornerShape(
                    topStart = MaterialTheme.shapes.extraSmall.topStart,
                    topEnd = MaterialTheme.shapes.extraSmall.topEnd,
                    bottomStart = MaterialTheme.shapes.large.bottomStart,
                    bottomEnd = MaterialTheme.shapes.large.bottomEnd,
                ),
            supportingText = { Text(stringResource(Res.string.description_write_an_email)) },
        )
    }
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
