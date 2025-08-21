package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.exclusiveTransaction
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.maksimowiczm.findmyip.application.infrastructure.transaction.TransactionProvider
import com.maksimowiczm.findmyip.application.infrastructure.transaction.TransactionScope

@Database(
    entities = [AddressHistoryEntity::class],
    version = FindMyIpDatabase.VERSION,
    exportSchema = false,
)
@TypeConverters(AddressVersionTypeConverter::class)
internal abstract class FindMyIpDatabase : RoomDatabase(), TransactionProvider {

    abstract val addressHistoryDao: AddressHistoryDao

    override suspend fun <T> immediate(block: suspend TransactionScope<T>.() -> T): T =
        useWriterConnection {
            it.immediateTransaction {
                val scope = RoomTransactionScope(this)
                block(scope)
            }
        }

    override suspend fun <T> exclusive(block: suspend TransactionScope<T>.() -> T): T =
        useWriterConnection {
            it.exclusiveTransaction {
                val scope = RoomTransactionScope(this)
                block(scope)
            }
        }

    companion object {
        const val VERSION = 1

        fun Builder<FindMyIpDatabase>.buildDatabase(): FindMyIpDatabase {
            return build()
        }
    }
}
