package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.domain.mapper.AddressMapper
import com.maksimowiczm.findmyip.domain.notification.AddressNotificationService
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences
import com.maksimowiczm.findmyip.domain.source.AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.source.NetworkAddress

fun interface ProcessAddressUseCase {
    suspend fun process(networkAddress: NetworkAddress)
}

class ProcessAddressUseCaseImpl(
    private val notificationPreferences: NotificationPreferences,
    private val addressNotificationService: AddressNotificationService,
    private val addressLocalDataSource: AddressLocalDataSource,
    private val addressMapper: AddressMapper
) : ProcessAddressUseCase {
    override suspend fun process(networkAddress: NetworkAddress) {
        val entity = addressLocalDataSource.insertAddressIfUniqueToLast(
            addressMapper.toEntity(networkAddress)
        ) ?: return

        val address = addressMapper.toDomain(entity)

        if (notificationPreferences.shouldPostNotification(address)) {
            addressNotificationService.postAddress(address)
        }
    }
}
