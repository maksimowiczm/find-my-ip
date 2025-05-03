package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.domain.mapper.AddressMapper
import com.maksimowiczm.findmyip.domain.source.AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.AddressState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun interface ObserveCurrentAddressUseCase {
    fun observe(): Flow<AddressState>
}

class ObserveCurrentAddressUseCaseImpl(
    private val addressObserver: AddressObserver,
    private val addressLocalDataSource: AddressLocalDataSource,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : ObserveCurrentAddressUseCase {
    override fun observe() = addressObserver.flow.onEach {
        if (it is AddressState.Success) {
            coroutineScope.launch {
                val address = AddressMapper.toEntity(it.address)
                addressLocalDataSource.insertAddressIfUniqueToLast(address)
            }
        }
    }
}
