package com.maksimowiczm.findmyip.shared.core.infrastructure.room

import androidx.room.TransactionScope
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.transaction.TransactionScope as DomainTransactionScope

class RoomTransactionScope<T>(private val scope: TransactionScope<T>) : DomainTransactionScope<T> {
    override suspend fun rollback(result: T) = scope.rollback(result)
}
