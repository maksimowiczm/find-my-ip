package com.maksimowiczm.findmyip.ui.page.settings.notifications

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.component.SwitchSettingListItem
import com.maksimowiczm.findmyip.ui.component.SwitchSettingListItemTestTags
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsPage(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationsPageViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Android33NotificationsPage(
            state = state,
            onIntent = remember(viewModel) { viewModel::onIntent },
            onBack = onBack,
            modifier = modifier
        )
    } else {
        AndroidNotificationsPage(
            state = state,
            onIntent = remember(viewModel) { viewModel::onIntent },
            onBack = onBack,
            modifier = modifier
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Android33NotificationsPage(
    state: NotificationsPageState,
    onIntent: (NotificationsPageIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current
    var requestInSettings by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (
            activity != null &&
            !isGranted &&
            !shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)
        ) {
            requestInSettings = true
        }

        if (isGranted) {
            onIntent(NotificationsPageIntent.ToggleNotifications(true))
        }
    }

    val permissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    if (requestInSettings && !permissionState.status.isGranted) {
        AndroidNotificationsRedirectToSettingsAlertDialog(
            onDismissRequest = { requestInSettings = false },
            onConfirm = {
                activity?.let { redirectToNotificationsSettings(it) }
                requestInSettings = false
            }
        )
    }

    AndroidNotificationsPage(
        state = state.applyNotificationsPermission(),
        onIntent = {
            when (it) {
                is NotificationsPageIntent.ToggleNotifications
                if (it.newState == true && !permissionState.status.isGranted) -> {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

                else -> onIntent(it)
            }
        },
        onBack = onBack,
        modifier = modifier
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationsPageState.applyNotificationsPermission(): NotificationsPageState {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val state = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

        return remember(state.status, this) {
            if (state.status.isGranted) {
                this
            } else {
                NotificationsPageState.Disabled
            }
        }
    } else {
        return this
    }
}

@Composable
private fun AndroidNotificationsRedirectToSettingsAlertDialog(
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

@Composable
private fun AndroidNotificationsPage(
    state: NotificationsPageState,
    onIntent: (NotificationsPageIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    NotificationsPage(
        state = state,
        onIntent = onIntent,
        onBack = onBack,
        onSystemSettings = { redirectToNotificationsSettings(context) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun NotificationsPage(
    state: NotificationsPageState,
    onIntent: (NotificationsPageIntent) -> Unit,
    onBack: () -> Unit,
    onSystemSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.headline_notifications),
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag(NotificationsPageTestTags.BACK_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_go_back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        val outerPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() + 8.dp,
            start = paddingValues.calculateStartPadding(layoutDirection),
            end = paddingValues.calculateEndPadding(layoutDirection)
        )

        var cardHeight by remember { mutableIntStateOf(0) }
        val topPadding = remember(cardHeight) {
            density.run { cardHeight.toDp() } + 16.dp
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(outerPadding)
        ) {
            Surface(
                onClick = {
                    onIntent(NotificationsPageIntent.ToggleNotifications(!state.isEnabled))
                },
                modifier = Modifier
                    .zIndex(10f)
                    .onGloballyPositioned {
                        cardHeight = it.size.height
                    }
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.headline_notify_on_ip_change),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    modifier = Modifier.padding(vertical = 16.dp),
                    trailingContent = {
                        Switch(
                            checked = state.isEnabled,
                            onCheckedChange = {
                                onIntent(NotificationsPageIntent.ToggleNotifications(it))
                            },
                            modifier = Modifier.testTag(SwitchSettingListItemTestTags.SWITCH)
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                        headlineColor = LocalContentColor.current
                    )
                )
            }

            if (state is NotificationsPageState.Enabled) {
                LazyColumn(
                    modifier = Modifier.testTag(NotificationsPageTestTags.ENABLED_CONTENT),
                    contentPadding = PaddingValues(
                        top = topPadding,
                        bottom = paddingValues.calculateBottomPadding()
                    )
                ) {
                    item {
                        NetworkTypeSettings(
                            state = state,
                            onIntent = onIntent
                        )
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }

                    item {
                        InternetProtocolSettings(
                            state = state,
                            onIntent = onIntent
                        )
                    }

                    item {
                        HorizontalDivider()
                    }

                    item {
                        ListItem(
                            headlineContent = {
                                Text(
                                    stringResource(R.string.headline_system_notifications_settings)
                                )
                            },
                            modifier = Modifier
                                .testTag(NotificationsPageTestTags.SYSTEM_NOTIFICATIONS_SETTINGS)
                                .clickable { onSystemSettings() },
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = topPadding)
                        .testTag(NotificationsPageTestTags.DISABLED_CONTENT),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null
                    )

                    Text(
                        text = stringResource(R.string.neutral_you_havent_allowed_notifications),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun NetworkTypeSettings(
    state: NotificationsPageState.Enabled,
    onIntent: (NotificationsPageIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.network_type),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.description_network_type),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(8.dp))

        SwitchSettingListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.wifi)
                )
            },
            checked = state.wifiEnabled,
            onCheckedChange = {
                onIntent(NotificationsPageIntent.ToggleWifi(it))
            },
            leadingContent = {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NetworkWifi,
                        contentDescription = null
                    )
                }
            }
        )

        SwitchSettingListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.cellular)
                )
            },
            checked = state.cellularEnabled,
            onCheckedChange = {
                onIntent(NotificationsPageIntent.ToggleCellular(it))
            },
            leadingContent = {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NetworkCell,
                        contentDescription = null
                    )
                }
            }
        )

        SwitchSettingListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.vpn)
                )
            },
            checked = state.vpnEnabled,
            onCheckedChange = {
                onIntent(NotificationsPageIntent.ToggleVpn(it))
            },
            leadingContent = {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.VpnKey,
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Composable
private fun InternetProtocolSettings(
    state: NotificationsPageState.Enabled,
    onIntent: (NotificationsPageIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.internet_protocol),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.description_internet_protocol),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(8.dp))

        SwitchSettingListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.ipv4)
                )
            },
            checked = state.ipv4Enabled,
            onCheckedChange = {
                onIntent(NotificationsPageIntent.ToggleIpv4(it))
            }
        )

        SwitchSettingListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.ipv6)
                )
            },
            checked = state.ipv6Enabled,
            onCheckedChange = {
                onIntent(NotificationsPageIntent.ToggleIpv6(!state.ipv6Enabled))
            }
        )
    }
}

private fun redirectToNotificationsSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }

    context.startActivity(intent)
}

@Preview
@Composable
private fun NotificationsPagePreview() {
    AndroidNotificationsPage(
        state = NotificationsPageState.Disabled,
        onIntent = {},
        onBack = {}
    )
}
