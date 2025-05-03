package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.domain.mapper.AddressMapper
import com.maksimowiczm.findmyip.domain.model.AddressId
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
    private val handleNewAddressUseCase: HandleNewAddressUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : ObserveCurrentAddressUseCase {
    override fun observe() = addressObserver.flow.onSuccess {
        val entity = AddressMapper.toEntity(it.address)
        val newId = addressLocalDataSource.insertAddressIfUniqueToLast(entity)

        if (newId != null) {
            val address = AddressMapper.toDomain(it.address, AddressId(newId))
            handleNewAddressUseCase.handle(address)
        }
    }

    private fun Flow<AddressState>.onSuccess(
        action: suspend (AddressState.Success) -> Unit
    ): Flow<AddressState> = onEach { state ->
        if (state is AddressState.Success) {
            coroutineScope.launch {
                action(state)
            }
        }
    }
}
