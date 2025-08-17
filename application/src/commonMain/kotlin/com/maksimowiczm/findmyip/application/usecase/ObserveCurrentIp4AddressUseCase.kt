package com.maksimowiczm.findmyip.application.usecase

import com.maksimowiczm.findmyip.application.infrastructure.Ip4AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.Ip4AddressRemoteDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun interface ObserveCurrentIp4AddressUseCase {
    fun observe(): Flow<AddressStatus<Ip4Address>>
}

internal class ObserveCurrentIp4AddressUseCaseImpl(
    private val localSource: Ip4AddressLocalDataSource,
    private val remoteSource: Ip4AddressRemoteDataSource,
) : ObserveCurrentIp4AddressUseCase {

    override fun observe(): Flow<AddressStatus<Ip4Address>> =
        localSource
            .observeCurrentIp4Address()
            .map<Ip4Address, AddressStatus<Ip4Address>> { address ->
                AddressStatus.Success(address)
            }
            .onStart {
                try {
                    val currentAddress = remoteSource.getCurrentIp4Address()
                    localSource.saveCurrentIp4Address(currentAddress)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    val error: AddressStatus.Error<Ip4Address> =
                        when (val message = e.message) {
                            null -> AddressStatus.Error.Unknown()
                            else -> AddressStatus.Error.Custom(message)
                        }

                    emit(error)
                }
            }
}
