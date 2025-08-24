package com.maksimowiczm.findmyip.feature.contribute.common.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.action_share
import findmyip.composeapp.generated.resources.action_translate
import findmyip.composeapp.generated.resources.action_write_an_email
import findmyip.composeapp.generated.resources.description_get_in_touch
import findmyip.composeapp.generated.resources.description_share_with_friends
import findmyip.composeapp.generated.resources.description_translate
import findmyip.composeapp.generated.resources.headline_get_in_touch
import findmyip.composeapp.generated.resources.headline_share_with_friends
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ContributeAction(
    val icon: ImageVector,
    val title: StringResource,
    val description: StringResource,
    val buttonLabel: StringResource,
    val action: () -> Unit,
) {
    companion object {
        fun shareWithFriends(onShare: () -> Unit) =
            ContributeAction(
                icon = Icons.Outlined.Share,
                title = Res.string.headline_share_with_friends,
                description = Res.string.description_share_with_friends,
                buttonLabel = Res.string.action_share,
                action = onShare,
            )

        fun getInTouch(onEmail: () -> Unit) =
            ContributeAction(
                icon = Icons.Outlined.Mail,
                title = Res.string.headline_get_in_touch,
                description = Res.string.description_get_in_touch,
                buttonLabel = Res.string.action_write_an_email,
                action = onEmail,
            )

        fun translate(onTranslate: () -> Unit) =
            ContributeAction(
                icon = Icons.Outlined.Translate,
                title = Res.string.action_translate,
                description = Res.string.description_translate,
                buttonLabel = Res.string.action_translate,
                action = onTranslate,
            )
    }
}
