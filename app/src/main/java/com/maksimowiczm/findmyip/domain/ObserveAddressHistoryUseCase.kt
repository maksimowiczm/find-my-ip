package com.maksimowiczm.findmyip.domain

import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
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
    operator fun invoke(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<List<AddressHistory>> {
        return repository.observeAddressHistory(internetProtocolVersion).map {
            it.map {
                AddressHistory(it.ip, formatDateUseCase(it.date))
            }
        }
    }
}
