package com.maksimowiczm.findmyip.feature.settings.internetprotocolversion

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.BuildConfig
import com.maksimowiczm.findmyip.feature.settings.SettingToggle
import findmyip.composeapp.generated.resources.Res
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
        onIpv4Change = viewModel::setIpv4,
        ipv6Enabled = ipv6Enabled,
        onIpv6Change = viewModel::setIpv6
    )
}

@Composable
private fun InternetProtocolVersionSettings(
    ipv4Enabled: Boolean,
    onIpv4Change: (Boolean) -> Unit,
    ipv6Enabled: Boolean,
    onIpv6Change: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        SettingToggle(
            headlineContent = { Text(stringResource(Res.string.ipv4)) },
            supportingContent = { Text(BuildConfig.IPV4_PROVIDER) },
            checked = ipv4Enabled,
            onCheckedChange = {
                if (!ipv4Enabled || ipv6Enabled) {
                    onIpv4Change(!ipv4Enabled)
                }
            },
            enabled = !(ipv4Enabled && !ipv6Enabled)
        )
        SettingToggle(
            headlineContent = { Text(stringResource(Res.string.ipv6)) },
            supportingContent = { Text(BuildConfig.IPV6_PROVIDER) },
            checked = ipv6Enabled,
            onCheckedChange = {
                if (ipv4Enabled || !ipv6Enabled) {
                    onIpv6Change(!ipv6Enabled)
                }
            },
            enabled = !(!ipv4Enabled && ipv6Enabled)
        )
    }
}
