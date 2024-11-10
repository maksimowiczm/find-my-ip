package com.maksimowiczm.whatismyip.data.repository

import com.maksimowiczm.whatismyip.data.network.PublicAddressDataSource
import javax.inject.Inject

class PublicAddressRepository @Inject constructor(
    private val publicAddressDataSource: PublicAddressDataSource
) {
    fun observeCurrentAddress() = publicAddressDataSource.observeCurrentAddress(autoFetch = true)

    suspend fun refreshCurrentAddress() = publicAddressDataSource.refreshCurrentAddress()
}