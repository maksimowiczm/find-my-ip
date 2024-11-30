package com.maksimowiczm.findmyip.currentaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.mapBoth
import com.maksimowiczm.findmyip.data.Keys
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.repository.PublicAddressRepository
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import com.maksimowiczm.findmyip.domain.ObserveCurrentAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class AddressViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
    observeCurrentAddressUseCase: ObserveCurrentAddressUseCase,
    private val publicAddressRepository: PublicAddressRepository
) : ViewModel() {
    private val isLoadingV4 = MutableStateFlow(true)
    private val isLoadingV6 = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val ipv4 = userPreferencesRepository.get(Keys.ipv4_enabled).flatMapLatest {
        if (it != true) {
            isLoadingV4.emit(false)
            return@flatMapLatest MutableStateFlow(AddressState.Disabled)
        }

        observeCurrentAddressUseCase(InternetProtocolVersion.IPv4).map {
            isLoadingV4.emit(false)
            it.mapBoth(
                success = { AddressState.Loaded(it.ip) },
                failure = { AddressState.Error }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = AddressState.Loading
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val ipv6 = userPreferencesRepository.get(Keys.ipv6_enabled).flatMapLatest {
        if (it != true) {
            isLoadingV6.emit(false)
            return@flatMapLatest MutableStateFlow(AddressState.Disabled)
        }

        observeCurrentAddressUseCase(InternetProtocolVersion.IPv6).map {
            isLoadingV6.emit(false)
            it.mapBoth(
                success = { AddressState.Loaded(it.ip) },
                failure = { AddressState.Error }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = AddressState.Loading
    )

    val isLoading = combine(
        isLoadingV4,
        isLoadingV6
    ) { isLoadingV4, isLoadingV6 ->
        isLoadingV4 || isLoadingV6
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = true
    )

    fun refresh() {
        viewModelScope.launch { refresh(InternetProtocolVersion.IPv4) }
        viewModelScope.launch { refresh(InternetProtocolVersion.IPv6) }
    }

    private suspend fun refresh(internetProtocolVersion: InternetProtocolVersion) {
        val state = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> ipv4
            InternetProtocolVersion.IPv6 -> ipv6
        }

        if (state.value is AddressState.Disabled) {
            return
        }

        when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> isLoadingV4.emit(true)
            InternetProtocolVersion.IPv6 -> isLoadingV6.emit(true)
        }

        // Delay because otherwise user won't believe that the refresh is working
        delay(200)
        publicAddressRepository.refreshCurrentAddress(internetProtocolVersion)
    }
}

sealed interface AddressState {
    object Loading : AddressState
    object Disabled : AddressState
    data class Loaded(val address: String) : AddressState
    data object Error : AddressState
}
