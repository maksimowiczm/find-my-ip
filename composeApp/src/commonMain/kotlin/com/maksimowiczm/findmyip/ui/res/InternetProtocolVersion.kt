package com.maksimowiczm.findmyip.ui.res

import androidx.compose.runtime.Composable
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import findmyip.composeapp.generated.resources.*
import findmyip.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

@Composable
fun InternetProtocolVersion.stringResource(): String = when (this) {
    InternetProtocolVersion.IPv4 -> stringResource(Res.string.ipv4)
    InternetProtocolVersion.IPv6 -> stringResource(Res.string.ipv6)
}
