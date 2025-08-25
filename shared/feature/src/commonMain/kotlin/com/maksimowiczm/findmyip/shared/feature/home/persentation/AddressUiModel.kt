package com.maksimowiczm.findmyip.shared.feature.home.persentation

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime

@Immutable
internal interface AddressUiModel {
    val internetProtocolVersion: InternetProtocolVersion
    val address: String
    val domain: String?
    val dateTime: LocalDateTime
    val networkType: NetworkType
}
