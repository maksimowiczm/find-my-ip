package com.maksimowiczm.findmyip.data.model

data class Address(
    val ip: String,
    val networkType: NetworkType,
    val protocol: InternetProtocolVersion
)
