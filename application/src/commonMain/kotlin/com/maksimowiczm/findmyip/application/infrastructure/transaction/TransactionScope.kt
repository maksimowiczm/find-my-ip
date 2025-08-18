package com.maksimowiczm.findmyip.application.infrastructure.transaction

interface TransactionScope<T> {
    suspend fun rollback(result: T)
}
