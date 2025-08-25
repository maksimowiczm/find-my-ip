package com.maksimowiczm.findmyip.shared.feature.contribute.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.config.AppConfig
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.headline_share_with_friends
import findmyip.composeapp.generated.resources.message_share_with_friends
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
actual fun shareWithFriends(): () -> Unit {
    val appConfig: AppConfig = koinInject()
    val context = LocalContext.current
    val shareMessage = stringResource(Res.string.message_share_with_friends, appConfig.appUrl)
    val shareIntent =
        remember(shareMessage) {
            Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }
        }

    val titleString = stringResource(Res.string.headline_share_with_friends)
    return remember(context, shareIntent, titleString) {
        {
            val chooser = Intent.createChooser(shareIntent, titleString)
            context.startActivity(chooser)
        }
    }
}
