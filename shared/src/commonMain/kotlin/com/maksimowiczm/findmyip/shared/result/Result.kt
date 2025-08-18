package com.maksimowiczm.findmyip.shared.result

sealed interface Result<T, E> {
    data class Success<T, E>(val value: T) : Result<T, E>

    data class Error<T, E>(val error: E) : Result<T, E>
}

@Suppress("FunctionName") fun <T, E> Ok(value: T): Result<T, E> = Result.Success(value)

@Suppress("FunctionName") fun <T, E> Err(error: E): Result<T, E> = Result.Error(error)

inline fun <T, E> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (this is Result.Success) action(value)
    return this
}

inline fun <T, E> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Error) action(error)
    return this
}
