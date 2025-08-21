package com.maksimowiczm.findmyip.application.infrastructure.dns

import com.maksimowiczm.findmyip.domain.entity.IpAddress
import java.net.InetAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AndroidSystemDnsService : DnsService {
    override suspend fun reverseLookup(ip: IpAddress): String? {
        val string = ip.stringRepresentation()
        val addr = InetAddress.getByName(string)
        val hostName: String? = withContext(Dispatchers.IO) { addr.hostName }
        return hostName
    }
}
