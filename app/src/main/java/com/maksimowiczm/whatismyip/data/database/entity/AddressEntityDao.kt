package com.maksimowiczm.whatismyip.data.database.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressEntityDao {
    @Query("SELECT * FROM addressentity ORDER BY timestamp DESC")
    fun observeAddresses(): Flow<List<AddressEntity>>

    @Query("SELECT * FROM addressentity ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestAddress(): AddressEntity?

    @Insert
    suspend fun insertAddress(addressEntity: AddressEntity)

    @Query("DELETE FROM addressentity")
    suspend fun deleteAll()
}
