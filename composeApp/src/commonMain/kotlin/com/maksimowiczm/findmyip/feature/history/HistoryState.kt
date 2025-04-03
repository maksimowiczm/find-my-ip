package com.maksimowiczm.findmyip.feature.history

import androidx.compose.runtime.Immutable
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class Filter(val filterName: StringResource, val protocol: InternetProtocolVersion?) {
    All(Res.string.filter_all, null),
    Ipv4(Res.string.ipv4, InternetProtocolVersion.IPv4),
    Ipv6(Res.string.ipv6, InternetProtocolVersion.IPv6)
}

@Immutable
data class HistoryItem(
    val id: Long,
    val ip: String,
    val date: String,
    val protocol: InternetProtocolVersion
)
