package com.maksimowiczm.whatismyip.domain

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.maksimowiczm.whatismyip.data.Keys
import com.maksimowiczm.whatismyip.data.model.Address
import com.maksimowiczm.whatismyip.data.repository.PublicAddressRepository
import com.maksimowiczm.whatismyip.data.repository.UserPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveCurrentAddressUseCase @Inject constructor(
    private val publicAddressRepository: PublicAddressRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Result<Address, Unit>> {
        return combine(
            publicAddressRepository.observeCurrentAddress(),
            userPreferencesRepository.get(Keys.save_history)
        ) { addressResult, saveHistoryResult ->
            val address = addressResult ?: return@combine Err(Unit)

            if (saveHistoryResult == true) {
                // TODO
            }

            return@combine Ok(address)
        }
    }
}
