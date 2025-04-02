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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.data.IpifyConfig
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun InternetProtocolSettingsScreen(modifier: Modifier = Modifier) {
    InternetProtocolSettingsScreen(
        modifier = modifier,
        viewModel = koinViewModel()
    )
}

@Composable
internal fun InternetProtocolSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: InternetProtocolSettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    InternetProtocolSettingsScreen(
        state = state,
        onIpv4Toggle = remember(viewModel) {
            { enabled: Boolean -> viewModel.toggleIPv4(enabled) }
        },
        onIpv6Toggle = remember(viewModel) {
            { enabled: Boolean -> viewModel.toggleIPv6(enabled) }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InternetProtocolSettingsScreen(
    state: InternetProtocolSettingsState,
    onIpv4Toggle: (Boolean) -> Unit,
    onIpv6Toggle: (Boolean) -> Unit,
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
                    modifier = Modifier.clickable { onIpv4Toggle(!state.ipv4) },
                    supportingContent = { Text(IpifyConfig.IPV4) },
                    trailingContent = {
                        Switch(
                            checked = state.ipv4,
                            enabled = state.canSwitchIpv4,
                            onCheckedChange = onIpv4Toggle
                        )
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text(stringResource(Res.string.ipv6)) },
                    modifier = Modifier.clickable { onIpv6Toggle(!state.ipv6) },
                    supportingContent = { Text(IpifyConfig.IPV6) },
                    trailingContent = {
                        Switch(
                            checked = state.ipv6,
                            enabled = state.canSwitchIpv6,
                            onCheckedChange = onIpv6Toggle
                        )
                    }
                )
            }
        }
    }
}
