package com.maksimowiczm.findmyip.ui.component

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R
import kotlin.apply

@Composable
fun AndroidNotificationsRedirectToSettingsAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.action_go_to_settings))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.action_cancel))
            }
        },
        title = {
            Text(stringResource(R.string.headline_permission_required))
        },
        text = {
            Text(
                stringResource(R.string.description_notifications_permission_required)
            )
        }
    )
}

fun redirectToNotificationsSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }

    context.startActivity(intent)
}
