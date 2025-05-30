package com.maksimowiczm.findmyip.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType

@Entity(tableName = "Address")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Long = 0L,
    @ColumnInfo(name = "Ip")
    val ip: String,
    @ColumnInfo(name = "InternetProtocol")
    val internetProtocol: InternetProtocol,
    @ColumnInfo(name = "NetworkType")
    val networkType: NetworkType,
    @ColumnInfo(name = "EpochMillis")
    val epochMillis: Long
)
