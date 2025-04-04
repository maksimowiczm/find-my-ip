package com.maksimowiczm.findmyip.feature.settings.backgroundservice

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.valentinilk.shimmer.shimmer
import findmyip.composeapp.generated.resources.*
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun BackgroundServiceSettingsScreen(modifier: Modifier) {
    BackgroundServiceSettingsScreen(
        modifier = modifier,
        viewModel = koinViewModel()
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun BackgroundServiceSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: BackgroundServiceSettingsViewModel = koinViewModel()
) {
    val inputEnabled by viewModel.inputEnabled.collectAsStateWithLifecycle()
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()

    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberPermissionState(
            permission = android.Manifest.permission.POST_NOTIFICATIONS
        )

        LaunchedEffect(permissionState, viewModel) {
            snapshotFlow { permissionState.status }.collectLatest {
                if (it.isGranted) {
                    viewModel.enableNotifications()
                } else {
                    viewModel.disableNotifications()
                }
            }
        }

        permissionState
    } else {
        null
    }

    val onToggle = remember(viewModel) {
        { enabled: Boolean ->
            if (enabled) {
                if (
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    permissionState != null &&
                    !permissionState.status.isGranted
                ) {
                    permissionState.launchPermissionRequest()
                }

                viewModel.enable()
            } else {
                viewModel.disable()
            }
        }
    }

    val onNotificationsToggle = remember(viewModel) {
        { enable: Boolean ->
            if (enable) {
                if (
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    permissionState != null &&
                    !permissionState.status.isGranted
                ) {
                    if (!permissionState.status.isGranted) {
                        permissionState.launchPermissionRequest()
                    }
                }

                viewModel.enableNotifications()
            } else {
                viewModel.disableNotifications()
            }
        }
    }

    BackgroundServiceSettingsScreen(
        inputEnabled = inputEnabled,
        enabled = enabled ?: return,
        onToggle = onToggle,
        notificationsEnabled = notificationsEnabled,
        onNotificationsToggle = onNotificationsToggle,
        modifier = modifier
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BackgroundServiceSettingsScreen(
    inputEnabled: Boolean,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val interactionEnabled = inputEnabled

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.headline_background_service),
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                Card(
                    onClick = { onToggle(!enabled) },
                    enabled = interactionEnabled,
                    modifier = Modifier.padding(16.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = stringResource(Res.string.action_use_background_service),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = if (interactionEnabled) {
                                    Modifier
                                } else {
                                    Modifier.shimmer()
                                }
                            )
                        },
                        modifier = Modifier.padding(8.dp),
                        trailingContent = {
                            Switch(
                                checked = enabled,
                                enabled = interactionEnabled,
                                onCheckedChange = { onToggle(it) }
                            )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            }

            if (!enabled || !inputEnabled) {
                item {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(
                                Res.string.description_background_service_disabled
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            } else {
                item {
                    Text(
                        text = stringResource(Res.string.description_background_worker),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                item {
                    ListItem(
                        headlineContent = {
                            Text(
                                stringResource(
                                    Res.string.description_background_service_notification
                                )
                            )
                        },
                        modifier = Modifier.clickable {
                            onNotificationsToggle(!notificationsEnabled)
                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.MailOutline,
                                contentDescription = null
                            )
                        },
                        supportingContent = {
                            Text(
                                text = stringResource(
                                    Res.string.description_background_service_notification
                                )
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = {
                                    onNotificationsToggle(it)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}
