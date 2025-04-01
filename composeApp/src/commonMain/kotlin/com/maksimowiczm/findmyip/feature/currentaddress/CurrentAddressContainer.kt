package com.maksimowiczm.findmyip.feature.currentaddress

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.infrastructure.di.observe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.reduce
import pro.respawn.flowmvi.plugins.whileSubscribed

private typealias State = CurrentAddressState
private typealias Intent = CurrentAddressIntent
private typealias Action = CurrentAddressAction
private typealias Ctx = PipelineContext<State, Intent, Action>

internal class CurrentAddressContainer(
    private val addressRepository: AddressRepository,
    private val dataStore: DataStore<Preferences>
) : Container<State, Intent, Action> {
    override val store = store(CurrentAddressState()) {
        whileSubscribed {
            observeCurrentAddress()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Ctx.observeCurrentAddress() {
        val ipv4Flow = dataStore
            .observe(PreferenceKeys.ipv4Enabled)
            .flatMapLatest { enabled ->
                if (enabled == true) {
                    addressRepository.observeAddress(InternetProtocolVersion.IPv4)
                } else {
                    flowOf(null)
                }
            }

        val ipv6Flow = dataStore
            .observe(PreferenceKeys.ipv6Enabled)
            .flatMapLatest { enabled ->
                if (enabled == true) {
                    addressRepository.observeAddress(InternetProtocolVersion.IPv6)
                } else {
                    flowOf(null)
                }
            }

        combine(ipv4Flow, ipv6Flow) { ipv4, ipv6 ->
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
}

private fun Address?.toState(): IpAddressState = when (this) {
    is Address.Error -> IpAddressState.Error(message)
    Address.Loading -> IpAddressState.Loading
    is Address.Success -> IpAddressState.Success(ip)
    null -> IpAddressState.Disabled
}
