package com.maksimowiczm.whatismyip.domain

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.maksimowiczm.whatismyip.data.network.Address
import com.maksimowiczm.whatismyip.data.repository.PublicAddressRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveCurrentAddressUseCase @Inject constructor(
    private val publicAddressRepository: PublicAddressRepository
) {
    operator fun invoke(): Flow<Result<Address, Unit>> {
        return publicAddressRepository.observeCurrentAddress().map {
            when (it) {
                is Address -> Ok(it)
                null -> Err(Unit)
            }
        }
    }
}
