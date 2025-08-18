package com.maksimowiczm.findmyip.application.usecase

import com.maksimowiczm.findmyip.application.infrastructure.Ip6AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.Ip6AddressRemoteDataSource
import com.maksimowiczm.findmyip.shared.log.Logger
import com.maksimowiczm.findmyip.shared.result.Err
import com.maksimowiczm.findmyip.shared.result.Ok
import com.maksimowiczm.findmyip.shared.result.Result

data class RefreshIp6AddressError(val message: String?)

fun interface RefreshIp6AddressUseCase {
    suspend fun refresh(): Result<Unit, RefreshIp6AddressError>
}

internal class RefreshIp6AddressUseCaseImpl(
    private val remoteDataSource: Ip6AddressRemoteDataSource,
    private val localDataSource: Ip6AddressLocalDataSource,
    private val logger: Logger,
) : RefreshIp6AddressUseCase {

    override suspend fun refresh(): Result<Unit, RefreshIp6AddressError> =
        try {
            val currentAddress = remoteDataSource.getCurrentIp6Address()
            localDataSource.saveCurrentIp6Address(currentAddress)
            Ok(Unit)
        } catch (e: Exception) {
            logger.e(TAG, e) { "Failed to refresh current IP address" }
            Err(RefreshIp6AddressError(e.message))
        }

    private companion object {
        const val TAG = "RefreshIp6AddressUseCaseImpl"
    }
}
