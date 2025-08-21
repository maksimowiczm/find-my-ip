package com.maksimowiczm.findmyip.application.usecase

import com.maksimowiczm.findmyip.application.infrastructure.date.DateProvider
import com.maksimowiczm.findmyip.application.infrastructure.dns.DnsService
import com.maksimowiczm.findmyip.application.infrastructure.local.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentIp6AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.remote.Ip6AddressRemoteDataSource
import com.maksimowiczm.findmyip.application.infrastructure.transaction.TransactionProvider
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
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
    private val currentIp6: CurrentIp6AddressLocalDataSource,
    private val historyLocalDataSource: AddressHistoryLocalDataSource,
    private val transactionProvider: TransactionProvider,
    private val dateProvider: DateProvider,
    private val dnsService: DnsService,
    private val logger: Logger,
) : RefreshIp6AddressUseCase {

    override suspend fun refresh(): Result<Unit, RefreshIp6AddressError> {
        val now = dateProvider.now()

        return try {
            val currentAddress = remoteDataSource.getCurrentIp6Address()
            val domain = dnsService.reverseLookup(currentAddress)
            currentIp6.updateIp6(
                AddressStatus.Success(dateTime = now, address = currentAddress, domain = domain)
            )
            val history =
                AddressHistory.Ipv6(
                    id = 0,
                    address = currentAddress,
                    domain = domain,
                    dateTime = now,
                )

            transactionProvider.immediate {
                val latest = historyLocalDataSource.getLatestIp6Address()

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
                null -> AddressStatus.Error.Unknown<Ip6Address>(now)
                else -> AddressStatus.Error.Custom(now, message)
            }.let { currentIp6.updateIp6(it) }

            Err(RefreshIp6AddressError(e.message))
        }
    }

    private companion object {
        const val TAG = "RefreshIp6AddressUseCaseImpl"
    }
}
