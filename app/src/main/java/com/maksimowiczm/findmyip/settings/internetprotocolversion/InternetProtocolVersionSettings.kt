package com.maksimowiczm.findmyip.settings.internetprotocolversion

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.BuildConfig
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.settings.SettingToggle
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
internal fun InternetProtocolVersionSettings(
    modifier: Modifier = Modifier,
    viewModel: InternetProtocolVersionSettingsViewModel = hiltViewModel()
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
            headlineContent = { Text(stringResource(R.string.ipv4)) },
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
            headlineContent = { Text(stringResource(R.string.ipv6)) },
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

@PreviewLightDark
@Composable
private fun InternetProtocolVersionSettingsPreview() {
    FindMyIpAppTheme {
        Surface {
            InternetProtocolVersionSettings(
                ipv4Enabled = true,
                onIpv4Change = {},
                ipv6Enabled = false,
                onIpv6Change = {}
            )
        }
    }
}
