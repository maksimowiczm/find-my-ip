package com.maksimowiczm.findmyip.presentation.home

import androidx.compose.runtime.Immutable
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import kotlinx.datetime.LocalDateTime

@Immutable
data class AddressHistoryUiModel(
    val id: Long,
    val internetProtocolVersion: InternetProtocolVersion,
    val address: String,
    val domain: String?,
    val dateTime: LocalDateTime,
) {
    constructor(
        domain: AddressHistory
    ) : this(
        id = domain.id,
        internetProtocolVersion =
            when (domain) {
                is AddressHistory.Ipv4 -> InternetProtocolVersion.IPV4
                is AddressHistory.Ipv6 -> InternetProtocolVersion.IPV6
            },
        address = domain.stringRepresentation(),
        domain = domain.domain,
        dateTime = domain.dateTime,
    )
}
