package com.maksimowiczm.findmyip.application.usecase

import com.maksimowiczm.findmyip.application.infrastructure.date.DateProvider
import com.maksimowiczm.findmyip.application.infrastructure.local.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.remote.Ip4AddressRemoteDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import com.maksimowiczm.findmyip.shared.log.Logger
import com.maksimowiczm.findmyip.shared.result.Err
import com.maksimowiczm.findmyip.shared.result.Ok
import com.maksimowiczm.findmyip.shared.result.Result

data class RefreshIp4AddressError(val message: String?)

fun interface RefreshIp4AddressUseCase {
    suspend fun refresh(): Result<Unit, RefreshIp4AddressError>
}

internal class RefreshIp4AddressUseCaseImpl(
    private val remoteDataSource: Ip4AddressRemoteDataSource,
    private val historyLocalDataSource: AddressHistoryLocalDataSource,
    private val dateProvider: DateProvider,
    private val logger: Logger,
) : RefreshIp4AddressUseCase {

    override suspend fun refresh(): Result<Unit, RefreshIp4AddressError> =
        try {
            val currentAddress = remoteDataSource.getCurrentIp4Address()

            val history =
                AddressHistory.Ipv4(id = 0, address = currentAddress, dateTime = dateProvider.now())
            historyLocalDataSource.saveHistory(history)

            Ok(Unit)
        } catch (e: Exception) {
            logger.e(TAG, e) { "Failed to refresh current IP address" }
            Err(RefreshIp4AddressError(e.message))
        }

    private companion object {
        const val TAG = "RefreshIp4AddressUseCaseImpl"
    }
}
