package com.maksimowiczm.whatismyip.data.repository

import com.maksimowiczm.whatismyip.data.network.PublicAddressDataSource

class PublicAddressRepository(
    private val publicAddressDataSource: PublicAddressDataSource
) {
    fun observeCurrentAddress() = publicAddressDataSource.observeCurrentAddress(autoFetch = true)

    suspend fun refreshCurrentAddress() = publicAddressDataSource.refreshCurrentAddress()
}