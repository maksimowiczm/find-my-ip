package com.maksimowiczm.findmyip.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion

@Entity
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ip: String,
    val timestamp: Long,
    // Default value is set to "IPv4" because IPv6 was not supported in the initial release
    @ColumnInfo(defaultValue = "IPv4")
    val internetProtocolVersion: InternetProtocolVersion
)
