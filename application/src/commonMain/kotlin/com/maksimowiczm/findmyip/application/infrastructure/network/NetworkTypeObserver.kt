package com.maksimowiczm.findmyip.application.infrastructure.network

import com.maksimowiczm.findmyip.domain.entity.NetworkType

interface NetworkTypeObserver {
    fun getNetworkType(): NetworkType
}
