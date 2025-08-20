package com.maksimowiczm.findmyip.presentation.home

import androidx.compose.runtime.Immutable
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import kotlinx.datetime.LocalDateTime

@Immutable
data class AddressHistoryUiModel(
    val id: Long,
    val protocolVersion: ProtocolVersion,
    val address: String,
    val dateTime: LocalDateTime,
) {
    constructor(
        domain: AddressHistory
    ) : this(
        id = domain.id,
        protocolVersion =
            when (domain) {
                is AddressHistory.Ipv4 -> ProtocolVersion.IPV4
                is AddressHistory.Ipv6 -> ProtocolVersion.IPV6
            },
        address = domain.stringRepresentation(),
        dateTime = domain.dateTime,
    )
}
