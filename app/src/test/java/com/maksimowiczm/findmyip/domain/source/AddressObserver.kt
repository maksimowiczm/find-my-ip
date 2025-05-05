package com.maksimowiczm.findmyip.domain.source

import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import kotlinx.datetime.LocalDateTime

fun testNetworkAddress(
    ip: String = "127.0.0.1",
    networkType: NetworkType = NetworkType.WiFi,
    internetProtocol: InternetProtocol = InternetProtocol.IPv4,
    dateTime: LocalDateTime = LocalDateTime(2025, 5, 3, 19, 33)
): NetworkAddress = NetworkAddress(
    ip = ip,
    networkType = networkType,
    internetProtocol = internetProtocol,
    dateTime = dateTime
)
