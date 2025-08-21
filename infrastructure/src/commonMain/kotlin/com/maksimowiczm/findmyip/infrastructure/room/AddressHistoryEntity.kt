package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AddressHistory")
internal data class AddressHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val address: String,
    val addressVersion: AddressVersion,
    val epochSeconds: Long,
)
