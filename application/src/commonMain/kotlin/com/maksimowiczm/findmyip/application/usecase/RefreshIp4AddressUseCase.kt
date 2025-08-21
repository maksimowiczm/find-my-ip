package com.maksimowiczm.findmyip.application.usecase

import com.maksimowiczm.findmyip.application.infrastructure.date.DateProvider
import com.maksimowiczm.findmyip.application.infrastructure.dns.DnsService
import com.maksimowiczm.findmyip.application.infrastructure.local.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentIp4AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.remote.Ip4AddressRemoteDataSource
import com.maksimowiczm.findmyip.application.infrastructure.transaction.TransactionProvider
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
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
    private val currentIp4: CurrentIp4AddressLocalDataSource,
    private val historyLocalDataSource: AddressHistoryLocalDataSource,
    private val transactionProvider: TransactionProvider,
    private val dateProvider: DateProvider,
    private val dnsService: DnsService,
    private val logger: Logger,
) : RefreshIp4AddressUseCase {

    override suspend fun refresh(): Result<Unit, RefreshIp4AddressError> {
        val now = dateProvider.now()

        return try {
            val currentAddress = remoteDataSource.getCurrentIp4Address()
            val domain = dnsService.reverseLookup(currentAddress)
            currentIp4.updateIp4(
                AddressStatus.Success(dateTime = now, address = currentAddress, domain = domain)
            )
            val history =
                AddressHistory.Ipv4(
                    id = 0,
                    address = currentAddress,
                    domain = domain,
                    dateTime = now,
                )

            transactionProvider.immediate {
                val latest = historyLocalDataSource.getLatestIp4Address()

                if (
                    latest != null &&
                        latest.stringRepresentation() == currentAddress.stringRepresentation()
                ) {
                    logger.d(TAG) {
                        "Current IP address is the same as the latest one, skipping save."
                    }
                } else {
                    logger.d(TAG) { "Saving new current IP address: $currentAddress" }
                    historyLocalDataSource.saveHistory(history)
                }
            }

            Ok(Unit)
        } catch (e: Exception) {
            logger.e(TAG, e) { "Failed to refresh current IP address" }

            when (val message = e.message) {
                null -> AddressStatus.Error.Unknown<Ip4Address>(now)
                else -> AddressStatus.Error.Custom(now, message)
            }.let { currentIp4.updateIp4(it) }

            Err(RefreshIp4AddressError(e.message))
        }
    }

    private companion object {
        const val TAG = "RefreshIp4AddressUseCaseImpl"
    }
}
