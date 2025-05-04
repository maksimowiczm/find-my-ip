package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.domain.model.Address
import com.maksimowiczm.findmyip.domain.notification.AddressNotificationService
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences

/**
 * Use case for handling a new (distinct to previous) address.
 */
fun interface HandleNewAddressUseCase {
    suspend fun handle(newAddress: Address)
}

class HandleNewAddressUseCaseImpl(
    private val notificationPreferences: NotificationPreferences,
    private val addressNotificationService: AddressNotificationService
) : HandleNewAddressUseCase {
    override suspend fun handle(newAddress: Address) {
        if (notificationPreferences.shouldPostNotification(newAddress)) {
            addressNotificationService.postAddress(newAddress)
        }
    }
}
