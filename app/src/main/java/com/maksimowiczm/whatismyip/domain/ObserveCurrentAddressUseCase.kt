package com.maksimowiczm.whatismyip.domain

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

typealias Address = String

sealed interface ObserveCurrentAddressError

// TODO
class ObserveCurrentAddressUseCase {
    operator fun invoke(): Flow<Result<Address, ObserveCurrentAddressError>> {
        return flow {
            emit(Ok("127.0.0.1"))
        }
    }
}