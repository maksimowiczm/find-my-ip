package com.maksimowiczm.findmyip.feature.settings.addresshistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.history_network_type
import findmyip.composeapp.generated.resources.history_network_type_description
import findmyip.composeapp.generated.resources.mobile
import findmyip.composeapp.generated.resources.vpn
import findmyip.composeapp.generated.resources.wi_fi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NetworkTypeSettings(
    modifier: Modifier = Modifier,
    viewModel: NetworkTypeSettingsViewModel = koinViewModel()
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
                text = stringResource(Res.string.history_network_type),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(Res.string.history_network_type_description),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        ListItem(
            modifier = Modifier.clickable(onClick = { onWifiToggle(!wifiEnabled) }),
            headlineContent = { Text(stringResource(Res.string.wi_fi)) },
            leadingContent = { Checkbox(wifiEnabled, onWifiToggle) }
        )
        ListItem(
            modifier = Modifier.clickable(onClick = { onMobileToggle(!mobileEnabled) }),
            headlineContent = { Text(stringResource(Res.string.mobile)) },
            leadingContent = { Checkbox(mobileEnabled, onMobileToggle) }
        )
        ListItem(
            modifier = Modifier.clickable(onClick = { onVpnToggle(!vpnEnabled) }),
            headlineContent = { Text(stringResource(Res.string.vpn)) },
            leadingContent = { Checkbox(vpnEnabled, onVpnToggle) }
        )
    }
}
