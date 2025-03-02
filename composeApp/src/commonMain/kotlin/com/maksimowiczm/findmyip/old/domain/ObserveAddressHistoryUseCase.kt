package com.maksimowiczm.findmyip.old.domain

import com.maksimowiczm.findmyip.old.data.PublicAddressRepository
import com.maksimowiczm.findmyip.old.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class AddressHistory(val ip: String, val date: String)

class ObserveAddressHistoryUseCase(
    private val formatDateUseCase: FormatDateUseCase,
    private val repository: PublicAddressRepository
) {
    operator fun invoke(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<List<AddressHistory>> = repository.observeAddressHistory(internetProtocolVersion).map {
        it.map {
            AddressHistory(it.ip, formatDateUseCase(it.date))
        }
    }
}
