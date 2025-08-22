package com.maksimowiczm.findmyip.presentation.home

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime

@Immutable
interface AddressUiModel {
    val internetProtocolVersion: InternetProtocolVersion
    val address: String
    val domain: String?
    val dateTime: LocalDateTime
    val networkType: NetworkType
}
