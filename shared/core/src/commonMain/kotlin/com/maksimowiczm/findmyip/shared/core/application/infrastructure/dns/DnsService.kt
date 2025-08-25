package com.maksimowiczm.findmyip.shared.core.application.infrastructure.dns

import com.maksimowiczm.findmyip.shared.core.domain.IpAddress

interface DnsService {
    suspend fun reverseLookup(ip: IpAddress): String?
}
