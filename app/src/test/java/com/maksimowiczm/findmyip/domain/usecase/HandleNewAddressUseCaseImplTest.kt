package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.domain.model.testAddress
import com.maksimowiczm.findmyip.domain.notification.AddressNotificationService
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HandleNewAddressUseCaseImplTest {

    @Test
    fun `post notification notification enabled`() = runTest {
        val notificationPreferences = NotificationPreferences { true }
        val notificationService = mockk<AddressNotificationService>(relaxed = true)

        val useCase = HandleNewAddressUseCaseImpl(
            notificationPreferences = notificationPreferences,
            addressNotificationService = notificationService
        )

        useCase.handle(testAddress())

        verify(
            exactly = 1
        ) {
            notificationService.postAddress(testAddress())
        }
    }

    @Test
    fun `do not post notification notification disabled`() = runTest {
        val notificationPreferences = NotificationPreferences { false }
        val notificationService = mockk<AddressNotificationService>(relaxed = true)

        val useCase = HandleNewAddressUseCaseImpl(
            notificationPreferences = notificationPreferences,
            addressNotificationService = notificationService
        )

        useCase.handle(testAddress())

        verify(
            exactly = 0
        ) {
            notificationService.postAddress(testAddress())
        }
    }
}
