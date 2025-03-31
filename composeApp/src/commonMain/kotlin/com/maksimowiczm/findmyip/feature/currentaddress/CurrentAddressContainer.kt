package com.maksimowiczm.findmyip.feature.currentaddress

import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.reduce
import pro.respawn.flowmvi.plugins.whileSubscribed

internal class CurrentAddressContainer(private val addressRepository: AddressRepository) :
    Container<CurrentAddressState, CurrentAddressIntent, CurrentAddressAction> {
    override val store = store(CurrentAddressState()) {
        whileSubscribed {
            combine(
                addressRepository.observeAddress(InternetProtocolVersion.IPv4),
                addressRepository.observeAddress(InternetProtocolVersion.IPv6)
            ) { ipv4, ipv6 ->
                updateState {
                    CurrentAddressState(
                        ipv4 = ipv4.toState(),
                        ipv6 = ipv6.toState()
                    )
                }
            }.onStart {
                addressRepository.refreshAddresses()
            }.launchIn(this)
        }

        reduce {
            when (it) {
                is CurrentAddressIntent.Refresh -> {
                    updateState { CurrentAddressState() }
                    addressRepository.refreshAddresses()
                }
            }
        }
    }
}

private fun Address.toState(): IpAddressState = when (this) {
    is Address.Error -> IpAddressState.Error(message)
    Address.Loading -> IpAddressState.Loading
    is Address.Success -> IpAddressState.Success(ip)
}
