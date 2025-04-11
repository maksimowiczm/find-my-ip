package com.maksimowiczm.findmyip.feature.settings.history

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.SignalWifi4Bar
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.data.model.NetworkType
import com.valentinilk.shimmer.shimmer
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistorySettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: HistorySettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistorySettingsScreen(
        state = state,
        intent = remember(viewModel) { viewModel::onIntent },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun HistorySettingsScreen(
    state: HistorySettingsState,
    intent: (HistorySettingsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val transition = updateTransition(state)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.headline_history),
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
                CardButton(
                    state = state,
                    intent = intent,
                    transition = transition,
                    modifier = Modifier.padding(16.dp)
                )
            }

            when (state) {
                HistorySettingsState.Loading -> Unit

                HistorySettingsState.Disabled -> item {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(Res.string.description_history_settings_disabled),
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                is HistorySettingsState.Enabled -> enabledContent(
                    state = state,
                    intent = intent
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CardButton(
    state: HistorySettingsState,
    intent: (HistorySettingsIntent) -> Unit,
    modifier: Modifier = Modifier,
    transition: Transition<HistorySettingsState> = updateTransition(state)
) {
    val onToggle = remember(state) {
        {
            val intent = when (state) {
                HistorySettingsState.Loading -> null
                is HistorySettingsState.Disabled -> HistorySettingsIntent.EnableHistory
                is HistorySettingsState.Enabled -> HistorySettingsIntent.DisableHistory
            }

            if (intent != null) {
                intent(intent)
            }
        }
    }

    transition.Crossfade(
        contentKey = { it is HistorySettingsState.Loading },
        modifier = modifier
    ) {
        when (it) {
            HistorySettingsState.Loading -> Card(
                onClick = {},
                modifier = Modifier.shimmer(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(Res.string.action_use_history),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    modifier = Modifier.padding(8.dp),
                    trailingContent = {
                        Switch(
                            checked = state is HistorySettingsState.Enabled,
                            onCheckedChange = {}
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    )
                )
            }

            is HistorySettingsState.Loaded -> Card(
                onClick = onToggle,
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(Res.string.action_use_history),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    modifier = Modifier.padding(8.dp),
                    trailingContent = {
                        Switch(
                            checked = state is HistorySettingsState.Enabled,
                            onCheckedChange = {
                                when (it) {
                                    true -> intent(
                                        HistorySettingsIntent.EnableHistory
                                    )

                                    false -> intent(
                                        HistorySettingsIntent.DisableHistory
                                    )
                                }
                            }
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }
    }
}

private fun LazyListScope.enabledContent(
    state: HistorySettingsState.Enabled,
    intent: (HistorySettingsIntent) -> Unit
) {
    item {
        DuplicateIps(
            state = state,
            intent = intent
        )
    }

    item {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    item {
        NetworkType(
            state = state,
            intent = intent
        )
    }

    item {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    item {
        Worker(
            state = state,
            intent = intent
        )
    }
}

@Composable
private fun DuplicateIps(
    state: HistorySettingsState.Enabled,
    intent: (HistorySettingsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.headline_duplicate_ips),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(Res.string.description_duplicate_ips),
            style = MaterialTheme.typography.bodyMedium
        )

        val toggle = remember(state) {
            {
                intent(HistorySettingsIntent.ToggleDuplicates(!state.saveDuplicates))
            }
        }

        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(Res.string.action_save_duplicate_ips)
                )
            },
            modifier = Modifier.clickable { toggle() },
            trailingContent = {
                Switch(
                    checked = state.saveDuplicates,
                    onCheckedChange = { toggle() }
                )
            }
        )
    }
}

@Composable
private fun NetworkType(
    state: HistorySettingsState.Enabled,
    intent: (HistorySettingsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val toggle = remember(state) {
        { networkType: NetworkType ->
            when (networkType) {
                NetworkType.WIFI -> intent(HistorySettingsIntent.ToggleWifi(!state.wifiEnabled))
                NetworkType.MOBILE -> intent(
                    HistorySettingsIntent.ToggleCellularData(!state.cellularDataEnabled)
                )

                NetworkType.VPN -> intent(HistorySettingsIntent.ToggleVpn(!state.vpnEnabled))
                NetworkType.UNKNOWN -> Unit
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.headline_network_type),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(Res.string.description_network_type),
            style = MaterialTheme.typography.bodyMedium
        )

        ListItem(
            modifier = Modifier.clickable { toggle(NetworkType.WIFI) },
            headlineContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SignalWifi4Bar,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(Res.string.wi_fi)
                    )
                }
            },
            leadingContent = {
                Checkbox(
                    checked = state.wifiEnabled,
                    onCheckedChange = { toggle(NetworkType.WIFI) }
                )
            }
        )

        ListItem(
            modifier = Modifier.clickable { toggle(NetworkType.MOBILE) },
            headlineContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SignalCellular4Bar,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(Res.string.cellular_data)
                    )
                }
            },
            leadingContent = {
                Checkbox(checked = state.cellularDataEnabled, onCheckedChange = {
                    toggle(NetworkType.MOBILE)
                })
            }
        )

        ListItem(
            modifier = Modifier.clickable { toggle(NetworkType.VPN) },
            headlineContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VpnKey,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(Res.string.vpn)
                    )
                }
            },
            leadingContent = {
                Checkbox(checked = state.vpnEnabled, onCheckedChange = { toggle(NetworkType.VPN) })
            }
        )
    }
}

@Composable
private fun Worker(
    state: HistorySettingsState.Enabled,
    intent: (HistorySettingsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val onToggle = remember(intent) {
        { enabled: Boolean ->
            if (enabled) {
                intent(HistorySettingsIntent.ToggleWorker(true))
            } else {
                intent(HistorySettingsIntent.ToggleWorker(false))
            }
        }
    }

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
                onToggle(!state.workerEnabled)
            },
            trailingContent = {
                Switch(
                    checked = state.workerEnabled,
                    onCheckedChange = { onToggle(it) }
                )
            }
        )

        NotificationListItem(
            state = state,
            intent = intent
        )

        HorizontalDivider()

        ClearHistoryListItem(
            intent = intent
        )
    }
}

@Composable
expect fun NotificationListItem(
    state: HistorySettingsState.Enabled,
    intent: (HistorySettingsIntent) -> Unit,
    modifier: Modifier = Modifier
)

@Composable
private fun ClearHistoryListItem(
    intent: (HistorySettingsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        HistoryClearDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                intent(HistorySettingsIntent.ClearHistory)
                showDialog = false
            }
        )
    }

    ListItem(
        headlineContent = {
            Text(stringResource(Res.string.headline_clear_history))
        },
        modifier = modifier.clickable { showDialog = true },
        supportingContent = {
            Text(stringResource(Res.string.description_clear_history))
        }
    )
}

@Composable
private fun HistoryClearDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.headline_clear_history)) },
        text = {
            Text(
                text = stringResource(Res.string.description_clear_history_dialog)
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(Res.string.action_cancel))
            }
        },
        confirmButton = {
            TextButton(onConfirm) {
                Text(stringResource(Res.string.action_clear))
            }
        }
    )
}
