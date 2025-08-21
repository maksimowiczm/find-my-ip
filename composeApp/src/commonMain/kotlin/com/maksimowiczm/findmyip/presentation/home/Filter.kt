package com.maksimowiczm.findmyip.presentation.home

import androidx.compose.runtime.Immutable

@Immutable
data class Filter(val protocols: Set<InternetProtocolVersion>) {
    val filtersCount = protocols.size

    fun toggleInternetProtocol(protocol: InternetProtocolVersion): Filter =
        if (protocols.contains(protocol)) {
            copy(protocols = protocols - protocol)
        } else {
            copy(protocols = protocols + protocol)
        }
}
