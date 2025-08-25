package com.maksimowiczm.findmyip.shared.feature.home.persentation

import androidx.compose.runtime.Immutable
import com.maksimowiczm.findmyip.shared.core.domain.AddressHistory
import kotlinx.datetime.LocalDateTime

@Immutable
internal data class AddressHistoryUiModel(
    val id: Long,
    override val internetProtocolVersion: InternetProtocolVersion,
    override val address: String,
    override val domain: String?,
    override val dateTime: LocalDateTime,
    override val networkType: NetworkType,
) : AddressUiModel {
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
        networkType = NetworkType.fromDomain(domain.networkType),
    )
}
