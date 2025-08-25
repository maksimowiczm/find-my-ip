package com.maksimowiczm.findmyip.shared.core.application.infrastructure.transaction

interface TransactionProvider {
    suspend fun <T> immediate(block: suspend TransactionScope<T>.() -> T): T

    suspend fun <T> exclusive(block: suspend TransactionScope<T>.() -> T): T
}
