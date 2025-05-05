package com.maksimowiczm.findmyip.ui.page.history

import com.maksimowiczm.findmyip.domain.model.AddressId
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class HistoryPageState(
    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val internetProtocolsFilters: List<InternetProtocol> = emptyList(),
    val networkTypeFilters: List<NetworkType> = emptyList(),
    val dateRange: DateRange? = null,
    val addressList: List<Address> = emptyList()
)

data class Address(
    val id: AddressId,
    val ip: String,
    val dateTime: LocalDateTime,
    val networkType: NetworkType
) {
    companion object {
        fun fromDomain(address: com.maksimowiczm.findmyip.domain.model.Address): Address = Address(
            id = address.id,
            ip = address.ip,
            dateTime = address.dateTime,
            networkType = address.networkType
        )
    }
}

data class DateRange(val start: LocalDate, val end: LocalDate)
