package com.maksimowiczm.findmyip.network

import com.maksimowiczm.findmyip.data.model.NetworkType
import kotlinx.coroutines.flow.Flow

expect class ConnectivityObserver {
    fun observeNetworkType(): Flow<NetworkType?>
}
