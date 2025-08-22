package com.maksimowiczm.findmyip.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.SignalCellular4Bar
import androidx.compose.material.icons.outlined.SignalWifi4Bar
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.maksimowiczm.findmyip.presentation.home.NetworkType
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.cellular
import findmyip.composeapp.generated.resources.unknown
import findmyip.composeapp.generated.resources.vpn
import findmyip.composeapp.generated.resources.wifi
import org.jetbrains.compose.resources.stringResource

@Composable
fun NetworkType.stringResource(): String =
    when (this) {
        NetworkType.UNKNOWN -> stringResource(Res.string.unknown)
        NetworkType.WIFI -> stringResource(Res.string.wifi)
        NetworkType.CELLULAR -> stringResource(Res.string.cellular)
        NetworkType.VPN -> stringResource(Res.string.vpn)
    }

@Composable
fun NetworkType.Icon() {
    when (this) {
        NetworkType.UNKNOWN -> Icon(Icons.Outlined.QuestionMark, null)
        NetworkType.WIFI -> Icon(Icons.Outlined.SignalWifi4Bar, null)
        NetworkType.CELLULAR -> Icon(Icons.Outlined.SignalCellular4Bar, null)
        NetworkType.VPN -> Icon(Icons.Outlined.VpnKey, null)
    }
}
