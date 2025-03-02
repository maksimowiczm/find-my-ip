package com.maksimowiczm.findmyip.ui.settings.internetprotocol

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.BuildConfig
import com.maksimowiczm.findmyip.old.feature.settings.SettingToggle
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

        SettingToggle(
            headlineContent = { Text(stringResource(Res.string.ipv4)) },
            checked = ipv4Enabled ?: false,
            onCheckedChange = {
                if (ipv4Enabled == null || ipv6Enabled == null) return@SettingToggle

                if (!ipv4Enabled || ipv6Enabled) {
                    onIpv4Change(!ipv4Enabled)
                }
            },
            enabled = if (ipv4Enabled != null &&
                ipv6Enabled != null
            ) {
                !(ipv4Enabled && !ipv6Enabled)
            } else {
                false
            },
            supportingContent = { Text(BuildConfig.IPV4_PROVIDER) }
        )
        SettingToggle(
            headlineContent = { Text(stringResource(Res.string.ipv6)) },
            checked = ipv6Enabled ?: false,
            onCheckedChange = {
                if (ipv4Enabled == null || ipv6Enabled == null) return@SettingToggle

                if (ipv4Enabled || !ipv6Enabled) {
                    onIpv6Change(!ipv6Enabled)
                }
            },
            enabled = if (ipv4Enabled != null &&
                ipv6Enabled != null
            ) {
                !(!ipv4Enabled && ipv6Enabled)
            } else {
                false
            },
            supportingContent = { Text(BuildConfig.IPV6_PROVIDER) }
        )
    }
}
