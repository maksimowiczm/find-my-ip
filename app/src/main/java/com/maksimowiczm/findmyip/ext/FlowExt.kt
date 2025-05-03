package com.maksimowiczm.findmyip.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <T> Flow<List<T>>.filterValues(
    crossinline predicate: suspend (T) -> Boolean
): Flow<List<T>> = map { list ->
    list.filter { value -> predicate(value) }
}

inline fun <T, R> Flow<List<T>>.mapValues(
    crossinline transform: suspend (value: T) -> R
): Flow<List<R>> = map {
    it.map { value -> transform(value) }
}
