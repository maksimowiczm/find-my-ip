package com.maksimowiczm.findmyip.ui.page.settings.backgroundservices

import android.Manifest
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.domain.backgroundservices.ForegroundService
import com.maksimowiczm.findmyip.domain.backgroundservices.PeriodicWorker
import com.maksimowiczm.findmyip.ui.component.AndroidNotificationsRedirectToSettingsAlertDialog
import com.maksimowiczm.findmyip.ui.component.SwitchSettingListItem
import com.maksimowiczm.findmyip.ui.component.redirectToNotificationsSettings
import com.maksimowiczm.findmyip.ui.ext.add
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.compose.koinInject

@Composable
fun BackgroundServicesPage(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    periodicWorker: PeriodicWorker = koinInject(),
    foregroundService: ForegroundService = koinInject()
) {
    val coroutineScope = rememberCoroutineScope()

    val periodicWorkEnabled = periodicWorker.isRunning.collectAsStateWithLifecycle(
        runBlocking { periodicWorker.isRunning.first() }
    ).value

    val onTogglePeriodicWork: (Boolean) -> Unit = {
        coroutineScope.launch {
            if (it) {
                periodicWorker.start()
            } else {
                periodicWorker.stop()
            }
        }
    }

    val isServiceRunning = foregroundService.isRunning.collectAsStateWithLifecycle(
        runBlocking { foregroundService.isRunning.first() }
    ).value

    val onToggleForegroundService: (Boolean) -> Unit = {
        coroutineScope.launch {
            if (it) {
                foregroundService.start()
            } else {
                foregroundService.stop()
            }
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Android33PermissionProxy(
            onBack = onBack,
            foregroundServiceEnabled = isServiceRunning,
            onToggleForegroundService = onToggleForegroundService,
            periodicWorkEnabled = periodicWorkEnabled,
            onTogglePeriodicWork = onTogglePeriodicWork,
            modifier = modifier
        )
    } else {
        BackgroundServicesPage(
            onBack = onBack,
            foregroundServiceEnabled = isServiceRunning,
            onToggleForegroundService = onToggleForegroundService,
            periodicWorkEnabled = periodicWorkEnabled,
            onTogglePeriodicWork = onTogglePeriodicWork,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun Android33PermissionProxy(
    onBack: () -> Unit,
    foregroundServiceEnabled: Boolean,
    onToggleForegroundService: (Boolean) -> Unit,
    periodicWorkEnabled: Boolean,
    onTogglePeriodicWork: (Boolean) -> Unit,
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
            onToggleForegroundService(true)
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

    BackgroundServicesPage(
        onBack = onBack,
        foregroundServiceEnabled = foregroundServiceEnabled,
        onToggleForegroundService = {
            if (it) {
                if (permissionState.status.isGranted) {
                    onToggleForegroundService(true)
                } else {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                onToggleForegroundService(false)
            }
        },
        periodicWorkEnabled = periodicWorkEnabled,
        onTogglePeriodicWork = onTogglePeriodicWork,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundServicesPage(
    onBack: () -> Unit,
    foregroundServiceEnabled: Boolean,
    onToggleForegroundService: (Boolean) -> Unit,
    periodicWorkEnabled: Boolean,
    onTogglePeriodicWork: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.headline_background_services),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag(BackgroundServicesPageTestTags.GO_BACK_BUTTON)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag(BackgroundServicesPageTestTags.CONTENT)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = lazyListState,
            contentPadding = paddingValues.add(vertical = 8.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.description_background_services_long),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                SwitchSettingListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.headline_foreground_service)
                        )
                    },
                    checked = foregroundServiceEnabled,
                    onCheckedChange = onToggleForegroundService,
                    modifier = Modifier.testTag(
                        BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SETTING
                    ),
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.description_foreground_service)
                        )
                    }
                )
            }

            item {
                SwitchSettingListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.headline_periodic_work)
                        )
                    },
                    checked = periodicWorkEnabled,
                    onCheckedChange = onTogglePeriodicWork,
                    modifier = Modifier.testTag(
                        BackgroundServicesPageTestTags.PERIODIC_WORK_SETTING
                    ),
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.description_periodic_work)
                        )
                    }
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .testTag(BackgroundServicesPageTestTags.TIPS)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Tip(
                        title = {
                            Text(
                                text = stringResource(
                                    R.string.headline_foreground_service_and_periodic_work
                                )
                            )
                        },
                        description = {
                            Text(
                                text = stringResource(
                                    R.string.description_foreground_service_and_periodic_work
                                )
                            )
                        }
                    )

                    Tip(
                        title = {
                            Text(
                                text = stringResource(
                                    R.string.headline_what_is_network_state_change
                                )
                            )
                        },
                        description = {
                            Text(
                                text = stringResource(
                                    R.string.description_what_is_network_state_change
                                )
                            )
                        }
                    )

                    Tip(
                        title = {},
                        description = {
                            Text(
                                text = stringResource(
                                    R.string.description_combine_periodic_notifications
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
