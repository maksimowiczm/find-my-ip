package com.maksimowiczm.findmyip.shared.core.application.infrastructure.network

import com.maksimowiczm.findmyip.shared.core.domain.NetworkType

interface NetworkTypeObserver {
    fun getNetworkType(): NetworkType
}
