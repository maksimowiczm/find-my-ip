package com.maksimowiczm.findmyip.ui.page.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R
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

data class Address(val ip: String, val dateTime: LocalDateTime, val networkType: NetworkType)

sealed interface NetworkType {
    data object WiFi : NetworkType
    data object Cellular : NetworkType
    data object VPN : NetworkType

    @Composable
    fun stringResource(): String = when (this) {
        Cellular -> stringResource(R.string.cellular)
        VPN -> stringResource(R.string.vpn)
        WiFi -> stringResource(R.string.wifi)
    }
}

enum class InternetProtocol {
    IPv4,
    IPv6;

    @Composable
    fun stringResource(): String = when (this) {
        IPv4 -> stringResource(R.string.ipv4)
        IPv6 -> stringResource(R.string.ipv6)
    }
}

data class DateRange(val start: LocalDate, val end: LocalDate)
