package com.maksimowiczm.findmyip.application.usecase

import com.maksimowiczm.findmyip.application.infrastructure.DateProvider
import com.maksimowiczm.findmyip.application.infrastructure.Ip6AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.Ip6AddressRemoteDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import com.maksimowiczm.findmyip.shared.log.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun interface ObserveCurrentIp6AddressUseCase {
    fun observe(): Flow<AddressStatus<Ip6Address>>
}

internal class ObserveCurrentIp6AddressUseCaseImpl(
    private val localSource: Ip6AddressLocalDataSource,
    private val remoteSource: Ip6AddressRemoteDataSource,
    private val dateProvider: DateProvider,
    private val logger: Logger,
) : ObserveCurrentIp6AddressUseCase {

    override fun observe(): Flow<AddressStatus<Ip6Address>> =
        localSource
            .observeCurrentIp6Address()
            .map<_, AddressStatus<Ip6Address>> { address ->
                logger.d(TAG) { "Current IP address: $address" }
                AddressStatus.Success(date = dateProvider.now(), value = address)
            }
            .onStart {
                try {
                    val currentAddress = remoteSource.getCurrentIp6Address()
                    localSource.saveCurrentIp6Address(currentAddress)
                } catch (e: CancellationException) {
                    logger.d(TAG) { "I'm cancelled" }
                    throw e
                } catch (e: Exception) {
                    logger.e(TAG, e) { "Failed to fetch current IP address" }

                    val error: AddressStatus.Error<Ip6Address> =
                        when (val message = e.message) {
                            null -> AddressStatus.Error.Unknown(dateProvider.now())
                            else ->
                                AddressStatus.Error.Custom(
                                    date = dateProvider.now(),
                                    message = message,
                                )
                        }

                    emit(error)
                }
            }

    private companion object {
        const val TAG = "ObserveCurrentIp6AddressUseCaseImpl"
    }
}
