package com.maksimowiczm.findmyip.application.infrastructure.dns

import com.maksimowiczm.findmyip.domain.entity.IpAddress

interface DnsService {
    suspend fun reverseLookup(ip: IpAddress): String?
}
