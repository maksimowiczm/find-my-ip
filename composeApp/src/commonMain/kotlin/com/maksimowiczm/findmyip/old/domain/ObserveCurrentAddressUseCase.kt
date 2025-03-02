package com.maksimowiczm.findmyip.old.domain

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.maksimowiczm.findmyip.old.data.Keys
import com.maksimowiczm.findmyip.old.data.PublicAddressRepository
import com.maksimowiczm.findmyip.old.data.UserPreferencesRepository
import com.maksimowiczm.findmyip.old.data.model.Address
import com.maksimowiczm.findmyip.old.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.old.network.CurrentAddressState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

sealed interface ObserveCurrentAddressError {
    data object Disabled : ObserveCurrentAddressError
    data object Unavailable : ObserveCurrentAddressError
    data class Unknown(val error: Throwable? = null) : ObserveCurrentAddressError
}

/**
 * Observes the current public address for the given address type. If observing given address type
 * is disabled, error [ObserveCurrentAddressError.Disabled] is emitted.
 */
class ObserveCurrentAddressUseCase(
    private val publicAddressRepository: PublicAddressRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val decideSaveAddressInHistoryUseCase: DecideSaveAddressInHistoryUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<Result<Address, ObserveCurrentAddressError>> {
        val enabledFlow = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> userPreferencesRepository.observe(Keys.ipv4_enabled)
            InternetProtocolVersion.IPv6 -> userPreferencesRepository.observe(Keys.ipv6_enabled)
        }

        return enabledFlow.flatMapLatest { enabled ->
            if (enabled != true) {
                return@flatMapLatest flow { emit(Err(ObserveCurrentAddressError.Disabled)) }
            }

            return@flatMapLatest publicAddressRepository.observeCurrentAddress(
                internetProtocolVersion
            )
                .filterNot { it is CurrentAddressState.None || it is CurrentAddressState.Loading }
                .map { addressState ->
                    val address = when (addressState) {
                        is CurrentAddressState.Error ->
                            return@map Err(ObserveCurrentAddressError.Unknown(addressState.error))

                        CurrentAddressState.NetworkUnavailable ->
                            return@map Err(ObserveCurrentAddressError.Unavailable)

                        is CurrentAddressState.Success -> addressState.address

                        // These are filtered out above
                        CurrentAddressState.Loading,
                        CurrentAddressState.None -> throw IllegalStateException("Invalid state")
                    }

                    if (decideSaveAddressInHistoryUseCase(address)) {
                        publicAddressRepository.insertIfDistinct(address)
                    }

                    Ok(address)
                }
        }
    }
}
