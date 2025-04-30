package com.maksimowiczm.findmyip.ui.res

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.domain.model.InternetProtocol

@Composable
fun InternetProtocol.stringResource(): String = when (this) {
    InternetProtocol.IPv4 -> stringResource(R.string.ipv4)
    InternetProtocol.IPv6 -> stringResource(R.string.ipv6)
}
