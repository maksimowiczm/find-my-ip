package com.maksimowiczm.findmyip.shared.feature.home.persentation

import androidx.compose.runtime.Immutable

@Immutable
internal data class Filter(val protocols: Set<InternetProtocolVersion>) {
    val filtersCount = protocols.size

    fun toggleInternetProtocol(protocol: InternetProtocolVersion): Filter =
        if (protocols.contains(protocol)) {
            copy(protocols = protocols - protocol)
        } else {
            copy(protocols = protocols + protocol)
        }
}
