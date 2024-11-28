package com.maksimowiczm.findmyip.settings.addresshistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
internal fun NetworkTypeSettings(
    modifier: Modifier = Modifier,
    viewModel: NetworkTypeSettingsViewModel = hiltViewModel()
) {
    val wifiState by viewModel.wifiState.collectAsStateWithLifecycle()
    val mobileState by viewModel.mobileState.collectAsStateWithLifecycle()
    val vpnState by viewModel.vpnState.collectAsStateWithLifecycle()

    NetworkTypeSettings(
        wifiEnabled = wifiState,
        onWifiToggle = viewModel::setWifi,
        mobileEnabled = mobileState,
        onMobileToggle = viewModel::setMobile,
        vpnEnabled = vpnState,
        onVpnToggle = viewModel::setVpn,
        modifier = modifier
    )
}

@Composable
private fun NetworkTypeSettings(
    wifiEnabled: Boolean,
    onWifiToggle: (Boolean) -> Unit,
    mobileEnabled: Boolean,
    onMobileToggle: (Boolean) -> Unit,
    vpnEnabled: Boolean,
    onVpnToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.padding(top = 8.dp)
    ) {
        Column(
            Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.history_network_type),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.history_network_type_description),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        ListItem(
            modifier = Modifier.clickable(onClick = { onWifiToggle(!wifiEnabled) }),
            headlineContent = { Text(stringResource(R.string.wi_fi)) },
            leadingContent = { Checkbox(wifiEnabled, onWifiToggle) }
        )
        ListItem(
            modifier = Modifier.clickable(onClick = { onMobileToggle(!mobileEnabled) }),
            headlineContent = { Text(stringResource(R.string.mobile)) },
            leadingContent = { Checkbox(mobileEnabled, onMobileToggle) }
        )
        ListItem(
            modifier = Modifier.clickable(onClick = { onVpnToggle(!vpnEnabled) }),
            headlineContent = { Text(stringResource(R.string.vpn)) },
            leadingContent = { Checkbox(vpnEnabled, onVpnToggle) }
        )
    }
}

@PreviewLightDark
@Composable
private fun NetworkTypeSettingsPreview() {
    FindMyIpAppTheme {
        Surface {
            NetworkTypeSettings(
                wifiEnabled = true,
                onWifiToggle = {},
                mobileEnabled = true,
                onMobileToggle = {},
                vpnEnabled = false,
                onVpnToggle = {}
            )
        }
    }
}
