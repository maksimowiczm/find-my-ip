package com.maksimowiczm.findmyip.feature.currentaddress

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState

@Immutable
internal data class CurrentAddressState(
    val ipv4: IpAddressState = IpAddressState.Loading,
    val ipv6: IpAddressState = IpAddressState.Loading
) : MVIState

@Immutable
internal sealed interface IpAddressState {
    data object Loading : IpAddressState
    data object Disabled : IpAddressState
    data class Success(val ip: String) : IpAddressState
    data class Error(val message: String) : IpAddressState
}

@Immutable
internal sealed interface CurrentAddressIntent : MVIIntent {
    data object Refresh : CurrentAddressIntent
}

@Immutable
internal sealed interface CurrentAddressAction : MVIAction
