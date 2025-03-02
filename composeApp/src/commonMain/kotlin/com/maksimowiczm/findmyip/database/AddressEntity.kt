package com.maksimowiczm.findmyip.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maksimowiczm.findmyip.old.data.model.InternetProtocolVersion

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
