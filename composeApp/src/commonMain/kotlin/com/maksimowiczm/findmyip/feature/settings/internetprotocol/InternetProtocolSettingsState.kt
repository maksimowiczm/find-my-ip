package com.maksimowiczm.findmyip.feature.settings.internetprotocol

import androidx.compose.runtime.Immutable

@Immutable
internal data class InternetProtocolSettingsState(
    val canSwitchIpv4: Boolean,
    val ipv4: Boolean,
    val canSwitchIpv6: Boolean,
    val ipv6: Boolean
)
