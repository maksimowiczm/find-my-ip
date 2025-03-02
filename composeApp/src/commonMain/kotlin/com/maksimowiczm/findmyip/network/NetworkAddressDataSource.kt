package com.maksimowiczm.findmyip.network

import kotlinx.coroutines.flow.Flow

interface NetworkAddressDataSource {
    fun observeAddress(): Flow<Result<String?>>
    fun refreshAddress()
}
