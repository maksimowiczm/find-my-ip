package com.maksimowiczm.findmyip.presentation.currentaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIp4AddressUseCase
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CurrentAddressViewModel(observeCurrentIp4AddressUseCase: ObserveCurrentIp4AddressUseCase) :
    ViewModel() {

    val ip4Flow =
        observeCurrentIp4AddressUseCase
            .observe()
            .map {
                when (it) {
                    is AddressStatus.Error<*> -> AddressStatusModel.Error
                    is AddressStatus.Success<Ip4Address> ->
                        AddressStatusModel.Success(ip4 = it.value.stringRepresentation())
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(2_000),
                initialValue = AddressStatusModel.Loading,
            )

    fun refresh() {
        // TODO
    }
}
