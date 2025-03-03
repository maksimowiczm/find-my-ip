package com.maksimowiczm.findmyip.old.data.model

import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.model.NetworkType
import java.util.Date

data class Address(
    val ip: String,
    val date: Date,
    val networkType: NetworkType?,
    val internetProtocolVersion: InternetProtocolVersion
)
