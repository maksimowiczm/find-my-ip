package com.maksimowiczm.findmyip.domain

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.network.CurrentAddressState
import com.maksimowiczm.findmyip.data.repository.PublicAddressRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveCurrentAddressUseCase @Inject constructor(
    private val publicAddressRepository: PublicAddressRepository,
    private val decideSaveAddressInHistoryUseCase: DecideSaveAddressInHistoryUseCase
) {
    operator fun invoke(): Flow<Result<Address?, Unit>> {
        return publicAddressRepository.observeCurrentAddress().map { addressState ->
            val address = when (addressState) {
                is CurrentAddressState.Error -> return@map Err(Unit)

                CurrentAddressState.Loading,
                CurrentAddressState.None,
                CurrentAddressState.NetworkUnavailable -> return@map Ok(null)

                is CurrentAddressState.Success -> addressState.address
            }

            if (decideSaveAddressInHistoryUseCase(address)) {
                publicAddressRepository.insertIfDistinct(address)
            }

            Ok(address)
        }
    }
}
