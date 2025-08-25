package com.maksimowiczm.findmyip.feature.contibute.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.VolunteerActivism
import com.maksimowiczm.findmyip.shared.feature.contribute.ui.ContributeAction
import findmyip.composeapp.generated.resources.*

internal fun ContributeAction.Companion.sponsor(onSponsor: () -> Unit) =
    ContributeAction(
        icon = Icons.Outlined.VolunteerActivism,
        title = Res.string.headline_become_a_sponsor,
        description = Res.string.description_become_a_sponsor,
        buttonLabel = Res.string.action_sponsor,
        action = onSponsor,
    )

internal fun ContributeAction.Companion.featureRequest(onFeatureRequest: () -> Unit) =
    ContributeAction(
        icon = Icons.Outlined.Lightbulb,
        title = Res.string.headline_feature_request,
        description = Res.string.description_feature_request,
        buttonLabel = Res.string.action_feature_request,
        action = onFeatureRequest,
    )

internal fun ContributeAction.Companion.bugReport(onBugReport: () -> Unit) =
    ContributeAction(
        icon = Icons.Outlined.BugReport,
        title = Res.string.headline_bug_report,
        description = Res.string.description_bug_report,
        buttonLabel = Res.string.action_bug_report,
        action = onBugReport,
    )
