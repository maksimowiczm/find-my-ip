package com.maksimowiczm.findmyip.feature.settings.history

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.SignalWifi4Bar
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.shimmer
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistorySettingsScreen(modifier: Modifier = Modifier) {
    HistorySettingsScreen(
        modifier = modifier,
        viewModel = koinViewModel()
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun HistorySettingsScreen(
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
                transition.Crossfade(
                    contentKey = { it is HistorySettingsState.Loading }
                ) {
                    when (it) {
                        HistorySettingsState.Loading -> Card(
                            onClick = {},
                            modifier = Modifier
                                .shimmer()
                                .padding(16.dp),
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
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify
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

private fun LazyListScope.enabledContent(
    state: HistorySettingsState.Enabled,
    intent: (HistorySettingsIntent) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            text = stringResource(Res.string.headline_duplicate_ips),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }

    item {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.description_duplicate_ips),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify
        )
    }

    item {
        Spacer(Modifier.height(8.dp))
    }

    item {
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

    item {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    item {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            text = stringResource(Res.string.headline_network_type),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }

    item {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.description_network_type),
            style = MaterialTheme.typography.bodyMedium
        )
    }

    item {
        val toggle = remember(state) {
            {
                intent(HistorySettingsIntent.ToggleWifi(!state.wifiEnabled))
            }
        }

        ListItem(
            modifier = Modifier.clickable {
                toggle()
            },
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
                Checkbox(checked = state.wifiEnabled, onCheckedChange = { toggle() })
            }
        )
    }

    item {
        val toggle = remember(state) {
            {
                intent(HistorySettingsIntent.ToggleCellularData(!state.cellularDataEnabled))
            }
        }

        ListItem(
            modifier = Modifier.clickable {
                toggle()
            },
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
                Checkbox(checked = state.cellularDataEnabled, onCheckedChange = { toggle() })
            }
        )
    }

    item {
        val toggle = remember(state) {
            {
                intent(HistorySettingsIntent.ToggleVpn(!state.vpnEnabled))
            }
        }

        ListItem(
            modifier = Modifier.clickable {
                toggle()
            },
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
                Checkbox(checked = state.vpnEnabled, onCheckedChange = { toggle() })
            }
        )
    }
}
