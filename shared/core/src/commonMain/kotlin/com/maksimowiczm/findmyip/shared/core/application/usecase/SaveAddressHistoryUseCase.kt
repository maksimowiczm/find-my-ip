package com.maksimowiczm.findmyip.shared.core.application.usecase

import com.maksimowiczm.findmyip.shared.core.application.infrastructure.local.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.log.Logger
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.transaction.TransactionProvider
import com.maksimowiczm.findmyip.shared.core.domain.AddressHistory
import com.maksimowiczm.findmyip.shared.core.domain.Ip4Address
import com.maksimowiczm.findmyip.shared.core.domain.Ip6Address
import com.maksimowiczm.findmyip.shared.core.domain.IpAddress
import com.maksimowiczm.findmyip.shared.core.domain.NetworkType
import kotlinx.datetime.LocalDateTime

interface SaveAddressHistoryUseCase {
    suspend fun <A : IpAddress> save(
        address: A,
        domain: String?,
        networkType: NetworkType,
        dateTime: LocalDateTime,
    )
}

internal class SaveAddressHistoryUseCaseImpl(
    private val historyLocalDataSource: AddressHistoryLocalDataSource,
    private val transactionProvider: TransactionProvider,
    private val logger: Logger,
) : SaveAddressHistoryUseCase {
    override suspend fun <A : IpAddress> save(
        address: A,
        domain: String?,
        networkType: NetworkType,
        dateTime: LocalDateTime,
    ) {
        transactionProvider.immediate {
            val latest = historyLocalDataSource.getLatestAddressOfType(address)

            if (
                latest != null &&
                    latest.stringRepresentation() == address.stringRepresentation() &&
                    latest.networkType == networkType
            ) {
                logger.d(TAG) { "Current IP address is the same as the latest one, skipping save." }
            } else {
                logger.d(TAG) { "Saving new current IP address" }
                historyLocalDataSource.saveHistory(
                    createHistory(address, domain, networkType, dateTime)
                )
            }
        }
    }

    private suspend fun <A : IpAddress> AddressHistoryLocalDataSource.getLatestAddressOfType(
        address: A
    ): AddressHistory? =
        when (address) {
            is Ip4Address -> getLatestIp4Address()
            is Ip6Address -> getLatestIp6Address()
        }

    private fun <A : IpAddress> createHistory(
        address: A,
        domain: String?,
        networkType: NetworkType,
        dateTime: LocalDateTime,
    ): AddressHistory =
        when (address) {
            is Ip4Address ->
                AddressHistory.Ipv4(
                    id = 0,
                    address = address,
                    domain = domain,
                    networkType = networkType,
                    dateTime = dateTime,
                )

            is Ip6Address ->
                AddressHistory.Ipv6(
                    id = 0,
                    address = address,
                    domain = domain,
                    networkType = networkType,
                    dateTime = dateTime,
                )
        }

    companion object {
        private const val TAG = "SaveAddressHistoryUseCase"
    }
}
