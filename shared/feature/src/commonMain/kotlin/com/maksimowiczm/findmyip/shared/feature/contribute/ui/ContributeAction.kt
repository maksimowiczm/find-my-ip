package com.maksimowiczm.findmyip.shared.feature.contribute.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import findmyip.composeapp.generated.resources.*
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
