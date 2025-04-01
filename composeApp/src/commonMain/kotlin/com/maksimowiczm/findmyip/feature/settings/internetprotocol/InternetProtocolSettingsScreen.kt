package com.maksimowiczm.findmyip.feature.settings.internetprotocol

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.data.IpifyConfig
import com.maksimowiczm.findmyip.infrastructure.di.container
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe

@Composable
fun InternetProtocolSettingsScreen(modifier: Modifier = Modifier) {
    InternetProtocolSettingsScreen(
        modifier = modifier,
        container = container()
    )
}

@Composable
internal fun InternetProtocolSettingsScreen(
    modifier: Modifier = Modifier,
    container: InternetProtocolSettingsContainer = container()
) = with(container.store) {
    val state by subscribe()

    when (val state = state) {
        is InternetProtocolSettingsState.Loaded -> InternetProtocolSettingsScreen(
            state = state,
            modifier = modifier
        )

        InternetProtocolSettingsState.Loading -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntentReceiver<InternetProtocolSettingsIntent>.InternetProtocolSettingsScreen(
    state: InternetProtocolSettingsState.Loaded,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.headline_internet_protocol),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {
            item {
                Text(
                    text = stringResource(Res.string.description_internet_protocol),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                ListItem(
                    headlineContent = { Text(stringResource(Res.string.ipv4)) },
                    modifier = Modifier.clickable {
                        intent(InternetProtocolSettingsIntent.ToggleIPv4(!state.ipv4))
                    },
                    supportingContent = { Text(IpifyConfig.IPV4) },
                    trailingContent = {
                        Switch(
                            checked = state.ipv4,
                            enabled = state.canSwitchIpv4,
                            onCheckedChange = {
                                intent(InternetProtocolSettingsIntent.ToggleIPv4(it))
                            }
                        )
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text(stringResource(Res.string.ipv6)) },
                    modifier = Modifier.clickable {
                        intent(InternetProtocolSettingsIntent.ToggleIPv6(!state.ipv6))
                    },
                    supportingContent = { Text(IpifyConfig.IPV6) },
                    trailingContent = {
                        Switch(
                            checked = state.ipv6,
                            enabled = state.canSwitchIpv6,
                            onCheckedChange = {
                                intent(InternetProtocolSettingsIntent.ToggleIPv6(it))
                            }
                        )
                    }
                )
            }
        }
    }
}
