package com.maksimowiczm.findmyip.ui.settings.internetprotocol

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.BuildConfig
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.internet_protocol
import findmyip.composeapp.generated.resources.internet_protocol_description
import findmyip.composeapp.generated.resources.ipv4
import findmyip.composeapp.generated.resources.ipv6
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InternetProtocolVersionSettings(
    modifier: Modifier = Modifier,
    viewModel: InternetProtocolVersionSettingsViewModel = koinViewModel()
) {
    val ipv4Enabled by viewModel.ipv4Enabled.collectAsStateWithLifecycle()
    val ipv6Enabled by viewModel.ipv6Enabled.collectAsStateWithLifecycle()

    InternetProtocolVersionSettings(
        modifier = modifier,
        ipv4Enabled = ipv4Enabled,
        onIpv4Change = viewModel::onIpv4EnabledChanged,
        ipv6Enabled = ipv6Enabled,
        onIpv6Change = viewModel::onIpv6EnabledChanged
    )
}

@Composable
private fun InternetProtocolVersionSettings(
    ipv4Enabled: Boolean?,
    onIpv4Change: (Boolean) -> Unit,
    ipv6Enabled: Boolean?,
    onIpv6Change: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(Res.string.internet_protocol),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(Res.string.internet_protocol_description),
            style = MaterialTheme.typography.bodyMedium
        )

        fun ipv4onCheckedChange() {
            if (ipv4Enabled == null || ipv6Enabled == null) return

            if (!ipv4Enabled || ipv6Enabled) {
                onIpv4Change(!ipv4Enabled)
            }
        }
        ListItem(
            headlineContent = { Text(stringResource(Res.string.ipv4)) },
            modifier = Modifier.clickable { ipv4onCheckedChange() },
            supportingContent = { Text(BuildConfig.IPV4_PROVIDER) },
            trailingContent = {
                Switch(
                    checked = ipv4Enabled ?: false,
                    onCheckedChange = { ipv4onCheckedChange() },
                    enabled = if (ipv4Enabled != null &&
                        ipv6Enabled != null
                    ) {
                        !(ipv4Enabled && !ipv6Enabled)
                    } else {
                        false
                    }
                )
            }
        )

        fun ipv6onCheckedChange() {
            if (ipv4Enabled == null || ipv6Enabled == null) return

            if (ipv4Enabled || !ipv6Enabled) {
                onIpv6Change(!ipv6Enabled)
            }
        }
        ListItem(
            headlineContent = { Text(stringResource(Res.string.ipv6)) },
            modifier = Modifier.clickable { ipv6onCheckedChange() },
            supportingContent = { Text(BuildConfig.IPV6_PROVIDER) },
            trailingContent = {
                Switch(
                    checked = ipv6Enabled ?: false,
                    onCheckedChange = { ipv6onCheckedChange() },
                    enabled = if (ipv4Enabled != null &&
                        ipv6Enabled != null
                    ) {
                        !(!ipv4Enabled && ipv6Enabled)
                    } else {
                        false
                    }
                )
            }
        )
    }
}
