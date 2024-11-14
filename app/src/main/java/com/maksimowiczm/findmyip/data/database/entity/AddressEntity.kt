package com.maksimowiczm.findmyip.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ip: String,
    val timestamp: Long
)
