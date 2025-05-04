package com.maksimowiczm.findmyip.domain.usecase

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
    private val processAddressUseCase: ProcessAddressUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : ObserveCurrentAddressUseCase {
    override fun observe() = addressObserver.flow.onSuccess {
        processAddressUseCase.process(it.address)
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
