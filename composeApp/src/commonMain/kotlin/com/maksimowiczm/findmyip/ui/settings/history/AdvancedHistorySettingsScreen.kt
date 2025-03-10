package com.maksimowiczm.findmyip.ui.settings.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.data.model.NetworkType
import com.maksimowiczm.findmyip.ui.component.ToggleSettingsScaffold
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdvancedHistorySettingsScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistorySettingsViewModel = koinViewModel()
) {
    val isEnabled by viewModel.isEnabled.collectAsStateWithLifecycle()
    val saveDuplicates by viewModel.saveDuplicates.collectAsStateWithLifecycle()
    val networkTypeMap by viewModel.networkTypeSettings.collectAsStateWithLifecycle()

    AdvancedHistorySettingsScreen(
        onNavigateUp = onNavigateUp,
        enabled = isEnabled,
        onEnabledChange = viewModel::onEnableChange,
        saveDuplicates = saveDuplicates,
        onSaveDuplicatesChange = viewModel::onSaveDuplicatesChange,
        onNetworkTypeToggle = {
            viewModel.onNetworkTypeToggle(it, !networkTypeMap[it]!!)
        },
        networkTypeMap = networkTypeMap,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdvancedHistorySettingsScreen(
    onNavigateUp: () -> Unit,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    saveDuplicates: Boolean,
    onSaveDuplicatesChange: (Boolean) -> Unit,
    onNetworkTypeToggle: (NetworkType) -> Unit,
    networkTypeMap: Map<NetworkType, Boolean>,
    modifier: Modifier = Modifier
) {
    ToggleSettingsScaffold(
        check = enabled,
        onCheckedChange = onEnabledChange,
        enabled = true,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.headline_history_settings))
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.action_go_back)
                        )
                    }
                }
            )
        },
        disabledContent = {
            Text(
                text = stringResource(Res.string.description_history_settings_disabled),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        },
        enabledContent = {
            EnabledContent(
                saveDuplicates = saveDuplicates,
                onSaveDuplicatesChange = onSaveDuplicatesChange,
                onNetworkTypeToggle = onNetworkTypeToggle,
                networkTypeMap = networkTypeMap
            )
        },
        modifier = modifier
    )
}

@Composable
private fun EnabledContent(
    saveDuplicates: Boolean,
    onSaveDuplicatesChange: (Boolean) -> Unit,
    onNetworkTypeToggle: (NetworkType) -> Unit,
    networkTypeMap: Map<NetworkType, Boolean>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            DistinctAddressesSettings(
                enabled = saveDuplicates,
                onEnabledChange = onSaveDuplicatesChange
            )
        }
        item {
            Spacer(Modifier.height(8.dp))
        }
        item {
            NetworkTypeSettings(
                onToggle = onNetworkTypeToggle,
                networkTypeMap = networkTypeMap
            )
        }
        item {
            Spacer(Modifier.height(8.dp))
        }
        item {
            AdvancedHistoryPlatformSettings()
        }
        item {
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun DistinctAddressesSettings(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            text = stringResource(Res.string.headline_duplicate_ips),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.description_duplicate_ips),
            style = MaterialTheme.typography.bodyMedium
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(Res.string.action_save_duplicate_ips)
                )
            },
            modifier = Modifier.clickable { onEnabledChange(!enabled) },
            trailingContent = {
                Switch(
                    checked = enabled,
                    onCheckedChange = onEnabledChange
                )
            }
        )
    }
}

@Composable
private fun NetworkTypeSettings(
    onToggle: (NetworkType) -> Unit,
    networkTypeMap: Map<NetworkType, Boolean>,
    modifier: Modifier = Modifier
) {
    // Only show the network type settings if there are more than one network types
    if (networkTypeMap.size < 2) return

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp),
            text = stringResource(Res.string.headline_network_type),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.description_network_type),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))

        if (networkTypeMap.containsKey(NetworkType.WIFI)) {
            val wifi = networkTypeMap[NetworkType.WIFI]!!

            ListItem(
                modifier = Modifier.clickable(onClick = { onToggle(NetworkType.WIFI) }),
                headlineContent = { Text(stringResource(Res.string.wi_fi)) },
                leadingContent = { Checkbox(wifi, { onToggle(NetworkType.WIFI) }) }
            )
        }

        if (networkTypeMap.containsKey(NetworkType.MOBILE)) {
            val mobile = networkTypeMap[NetworkType.MOBILE]!!

            ListItem(
                modifier = Modifier.clickable(onClick = { onToggle(NetworkType.MOBILE) }),
                headlineContent = { Text(stringResource(Res.string.cellular_data)) },
                leadingContent = { Checkbox(mobile, { onToggle(NetworkType.MOBILE) }) }
            )
        }

        if (networkTypeMap.containsKey(NetworkType.VPN)) {
            val vpn = networkTypeMap[NetworkType.VPN]!!

            ListItem(
                modifier = Modifier.clickable(onClick = { onToggle(NetworkType.VPN) }),
                headlineContent = { Text(stringResource(Res.string.vpn)) },
                leadingContent = { Checkbox(vpn, { onToggle(NetworkType.VPN) }) }
            )
        }
    }
}

@Composable
expect fun AdvancedHistoryPlatformSettings(modifier: Modifier = Modifier)
