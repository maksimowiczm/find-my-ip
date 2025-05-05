package com.maksimowiczm.findmyip.ui.page.home

object HomePageTestTags {
    const val IPV4 = "ipv4"
    const val IPV6 = "ipv6"
    const val IPV4_NOT_DETECTED = "ipv4_not_detected"
    const val IPV6_NOT_DETECTED = "ipv6_not_detected"
    const val IPV4_SHIMMER = "ipv4_shimmer"
    const val IPV6_SHIMMER = "ipv6_shimmer"
    const val ERROR_CARD = "error_card"
    const val PROGRESS_INDICATOR = "progress_indicator"
    const val REFRESH_BUTTON = "refresh_button"
}

val IpState.ipv4TestTag
    get() = when (this) {
        is IpState.Loading -> when (ip) {
            null -> HomePageTestTags.IPV4_SHIMMER
            else -> HomePageTestTags.IPV4
        }

        is IpState.Success -> HomePageTestTags.IPV4
        IpState.NotDetected -> HomePageTestTags.IPV4_NOT_DETECTED
    }

val IpState.ipv6TestTag
    get() = when (this) {
        is IpState.Loading -> when (ip) {
            null -> HomePageTestTags.IPV6_SHIMMER
            else -> HomePageTestTags.IPV6
        }

        is IpState.Success -> HomePageTestTags.IPV6
        IpState.NotDetected -> HomePageTestTags.IPV6_NOT_DETECTED
    }
