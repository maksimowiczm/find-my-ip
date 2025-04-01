package com.maksimowiczm.findmyip.feature.settings.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.infrastructure.di.container
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe

@Composable
fun HistorySettingsScreen(modifier: Modifier = Modifier) {
    HistorySettingsScreen(
        modifier = modifier,
        container = container()
    )
}

@Composable
internal fun HistorySettingsScreen(
    modifier: Modifier = Modifier,
    container: HistorySettingsContainer = container()
) = with(container.store) {
    val state by subscribe()

    when (val state = state) {
        HistorySettingsState.Loading -> Surface { Spacer(Modifier.fillMaxSize()) }
        is HistorySettingsState.Loaded -> HistorySettingsScreen(
            state = state,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntentReceiver<HistorySettingsIntent>.HistorySettingsScreen(
    state: HistorySettingsState.Loaded,
    modifier: Modifier = Modifier
) {
    val onToggle = {
        val intent = when (state) {
            is HistorySettingsState.Disabled -> HistorySettingsIntent.EnableHistory
            is HistorySettingsState.Enabled -> HistorySettingsIntent.DisableHistory
        }
        intent(intent)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
                Card(
                    onClick = onToggle,
                    modifier = Modifier.padding(16.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.action_use_history),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = state is HistorySettingsState.Enabled,
                            onCheckedChange = {
                                when (it) {
                                    true -> intent(HistorySettingsIntent.EnableHistory)
                                    false -> intent(HistorySettingsIntent.DisableHistory)
                                }
                            }
                        )
                    }
                }
            }

            when (state) {
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
                    intentReceiver = this@HistorySettingsScreen
                )
            }
        }
    }
}

private fun LazyListScope.enabledContent(
    state: HistorySettingsState.Enabled,
    intentReceiver: IntentReceiver<HistorySettingsIntent>
) = with(intentReceiver) {
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
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp),
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
                Text(
                    text = stringResource(Res.string.wi_fi)
                )
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
                Text(
                    text = stringResource(Res.string.cellular_data)
                )
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
                Text(
                    text = stringResource(Res.string.vpn)
                )
            },
            leadingContent = {
                Checkbox(checked = state.vpnEnabled, onCheckedChange = { toggle() })
            }
        )
    }
}
