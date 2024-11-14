package com.maksimowiczm.findmyip.domain

import com.maksimowiczm.findmyip.data.repository.PublicAddressRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class AddressHistory(
    val ip: String,
    val date: String
)

class ObserveAddressHistoryUseCase @Inject constructor(
    private val formatDateUseCase: FormatDateUseCase,
    private val repository: PublicAddressRepository
) {
    operator fun invoke(): Flow<List<AddressHistory>> {
        return repository.observeAddressHistory().map {
            it.map {
                AddressHistory(it.ip, formatDateUseCase(it.date))
            }
        }
    }
}
