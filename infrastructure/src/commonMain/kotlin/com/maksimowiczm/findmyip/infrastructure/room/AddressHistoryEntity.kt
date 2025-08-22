package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AddressHistory")
internal data class AddressHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val address: String,
    val domain: String?,
    val addressVersion: AddressVersion,
    val networkType: NetworkType,
    val epochSeconds: Long,
)
