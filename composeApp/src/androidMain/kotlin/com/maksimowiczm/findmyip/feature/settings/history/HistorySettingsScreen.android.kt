package com.maksimowiczm.findmyip.feature.settings.history

import android.Manifest
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.description_notify_on_new_ip
import findmyip.composeapp.generated.resources.headline_notify_on_new_ip
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun NotificationListItem(
    state: HistorySettingsState.Enabled,
    intent: (HistorySettingsIntent) -> Unit,
    modifier: Modifier
) {
    val intent by rememberUpdatedState(intent)

    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS) {
            if (it) {
                intent(HistorySettingsIntent.ToggleNotification(true))
            }
        }
    } else {
        null
    }

    LaunchedEffect(permissionState) {
        if (permissionState != null && !permissionState.status.isGranted) {
            intent(HistorySettingsIntent.ToggleNotification(false))
        }
    }

    val onToggle = remember(intent) {
        { enabled: Boolean ->
            if (enabled) {
                if (permissionState != null && !permissionState.status.isGranted) {
                    permissionState.launchPermissionRequest()
                } else {
                    intent(HistorySettingsIntent.ToggleNotification(true))
                }
            } else {
                intent(HistorySettingsIntent.ToggleNotification(false))
            }
        }
    }

    val enabled by remember(state) {
        derivedStateOf { state.workerEnabled }
    }

    val colors = if (enabled) {
        ListItemDefaults.colors()
    } else {
        ListItemDefaults.colors(
            headlineColor = MaterialTheme.colorScheme.outline,
            supportingColor = MaterialTheme.colorScheme.outline
        )
    }

    ListItem(
        headlineContent = {
            Text(stringResource(Res.string.headline_notify_on_new_ip))
        },
        modifier = modifier.clickable(
            enabled = enabled
        ) {
            onToggle(!(state.notificationEnabled ?: false))
        },
        supportingContent = {
            Text(stringResource(Res.string.description_notify_on_new_ip))
        },
        trailingContent = {
            Switch(
                checked = state.notificationEnabled ?: false,
                enabled = enabled,
                onCheckedChange = { onToggle(it) }
            )
        },
        colors = colors
    )
}
