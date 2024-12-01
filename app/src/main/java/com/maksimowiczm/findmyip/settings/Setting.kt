package com.maksimowiczm.findmyip.settings

import kotlinx.serialization.Serializable

@Serializable
internal enum class Setting {
    SaveHistory,
    ClearHistory,
    InternetProtocolVersion
}
