package com.maksimowiczm.findmyip.ui.res

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.domain.model.NetworkType

@Composable
fun NetworkType.stringResource(): String = when (this) {
    NetworkType.Cellular -> stringResource(R.string.cellular)
    NetworkType.VPN -> stringResource(R.string.vpn)
    NetworkType.WiFi -> stringResource(R.string.wifi)
}
