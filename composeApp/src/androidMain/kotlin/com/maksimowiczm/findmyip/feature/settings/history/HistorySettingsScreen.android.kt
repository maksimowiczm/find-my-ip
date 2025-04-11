package com.maksimowiczm.findmyip.feature.settings.history

import android.Manifest
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.action_use_background_worker
import findmyip.composeapp.generated.resources.description_background_worker
import findmyip.composeapp.generated.resources.description_notify_on_new_ip
import findmyip.composeapp.generated.resources.headline_background_worker
import findmyip.composeapp.generated.resources.headline_notify_on_new_ip
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun BackgroundWorker(modifier: Modifier) {
    val viewModel: AndroidHistorySettingsViewModel = koinViewModel()

    val workerEnabled by viewModel.workerEnabled.collectAsStateWithLifecycle()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()

    BackgroundWorker(
        workerEnabled = workerEnabled,
        notificationsEnabled = notificationsEnabled,
        onWorkerToggle = remember(viewModel) { viewModel::toggleWorker },
        onNotificationsToggle = remember(viewModel) { viewModel::toggleNotifications },
        modifier = modifier
    )
}

@Composable
private fun BackgroundWorker(
    workerEnabled: Boolean,
    notificationsEnabled: Boolean?,
    onWorkerToggle: (Boolean) -> Unit,
    onNotificationsToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.headline_background_worker),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(Res.string.description_background_worker),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(Res.string.action_use_background_worker),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            modifier = Modifier.clickable {
                onWorkerToggle(!workerEnabled)
            },
            trailingContent = {
                Switch(
                    checked = workerEnabled,
                    onCheckedChange = onWorkerToggle
                )
            }
        )

        NotificationListItem(
            workerEnabled = workerEnabled,
            notificationsEnabled = notificationsEnabled,
            onToggleNotifications = onNotificationsToggle
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationListItem(
    workerEnabled: Boolean,
    notificationsEnabled: Boolean?,
    onToggleNotifications: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val onToggleNotifications by rememberUpdatedState(onToggleNotifications)
    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS) {
            if (it) {
                onToggleNotifications(true)
            }
        }
    } else {
        null
    }

    LaunchedEffect(permissionState) {
        if (permissionState != null && !permissionState.status.isGranted) {
            onToggleNotifications(false)
        }
    }

    val onToggle = remember(onToggleNotifications) {
        { enabled: Boolean ->
            if (enabled) {
                if (permissionState != null && !permissionState.status.isGranted) {
                    permissionState.launchPermissionRequest()
                } else {
                    onToggleNotifications(true)
                }
            } else {
                onToggleNotifications(false)
            }
        }
    }

    val colors = if (workerEnabled) {
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
            enabled = workerEnabled
        ) {
            onToggle(!(notificationsEnabled ?: false))
        },
        supportingContent = {
            Text(stringResource(Res.string.description_notify_on_new_ip))
        },
        trailingContent = {
            Switch(
                checked = notificationsEnabled ?: false,
                enabled = workerEnabled,
                onCheckedChange = { onToggle(it) }
            )
        },
        colors = colors
    )
}
