package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.TransactionScope
import com.maksimowiczm.findmyip.application.infrastructure.transaction.TransactionScope as DomainTransactionScope

internal class RoomTransactionScope<T>(private val scope: TransactionScope<T>) :
    DomainTransactionScope<T> {
    override suspend fun rollback(result: T) = scope.rollback(result)
}
