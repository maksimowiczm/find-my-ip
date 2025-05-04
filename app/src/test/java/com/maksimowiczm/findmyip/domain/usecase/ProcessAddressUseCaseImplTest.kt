package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.data.model.AddressEntity
import com.maksimowiczm.findmyip.domain.mapper.AddressMapper
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.domain.model.testAddress
import com.maksimowiczm.findmyip.domain.notification.AddressNotificationService
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences
import com.maksimowiczm.findmyip.domain.source.AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.source.testNetworkAddress
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ProcessAddressUseCaseImplTest {

    @Test
    fun `insert address`() = runTest {
        val localDataSource = mockk<AddressLocalDataSource>(relaxed = true)

        val useCase = ProcessAddressUseCaseImpl(
            notificationPreferences = mockk(relaxed = true),
            addressNotificationService = mockk(relaxed = true),
            addressLocalDataSource = localDataSource,
            addressMapper = mockk(relaxed = true)
        )

        useCase.process(testNetworkAddress())

        coVerify(exactly = 1) {
            localDataSource.insertAddressIfUniqueToLast(any())
        }
    }

    @Test
    fun `shortcut when not inserted`() = runTest {
        val networkAddress = testNetworkAddress()
        val entity = AddressEntity(
            id = 0,
            ip = "ip",
            internetProtocol = InternetProtocol.IPv4,
            networkType = NetworkType.WiFi,
            epochMillis = 0L
        )
        val notificationPreferences = mockk<NotificationPreferences>()
        val addressNotificationService = mockk<AddressNotificationService>()
        val localDataSource = mockk<AddressLocalDataSource> {
            coEvery { insertAddressIfUniqueToLast(entity) } returns null
        }
        val addressMapper = mockk<AddressMapper> {
            coEvery { toEntity(networkAddress, any()) } returns entity
        }

        val useCase = ProcessAddressUseCaseImpl(
            notificationPreferences = notificationPreferences,
            addressNotificationService = addressNotificationService,
            addressLocalDataSource = localDataSource,
            addressMapper = addressMapper
        )

        useCase.process(networkAddress)

        coVerify(exactly = 1) {
            localDataSource.insertAddressIfUniqueToLast(entity)
        }

        coVerify(inverse = true) {
            addressMapper.toDomain(entity, any())
            addressMapper.toDomain(any())
            addressNotificationService.postAddress(any())
            notificationPreferences.shouldPostNotification(any())
            addressNotificationService.postAddress(any())
        }
    }

    @Test
    fun `call should post notification when inserted`() = runTest {
        val networkAddress = testNetworkAddress()
        val entity = AddressEntity(
            id = 0,
            ip = "ip",
            internetProtocol = InternetProtocol.IPv4,
            networkType = NetworkType.WiFi,
            epochMillis = 0L
        )
        val domainAddress = testAddress()
        val notificationPreferences = mockk<NotificationPreferences>(relaxed = true)
        val addressNotificationService = mockk<AddressNotificationService>(relaxed = true)
        val localDataSource = mockk<AddressLocalDataSource> {
            coEvery { insertAddressIfUniqueToLast(any()) } returns entity
        }
        val addressMapper = mockk<AddressMapper> {
            coEvery { toEntity(networkAddress, any()) } returns entity
            coEvery { toDomain(entity, any()) } returns domainAddress
        }

        val useCase = ProcessAddressUseCaseImpl(
            notificationPreferences = notificationPreferences,
            addressNotificationService = addressNotificationService,
            addressLocalDataSource = localDataSource,
            addressMapper = addressMapper
        )

        useCase.process(networkAddress)

        coVerify(exactly = 1) {
            notificationPreferences.shouldPostNotification(domainAddress)
        }
    }

    @Test
    fun `post notification`() = runTest {
        val networkAddress = testNetworkAddress()
        val entity = AddressEntity(
            id = 0,
            ip = "ip",
            internetProtocol = InternetProtocol.IPv4,
            networkType = NetworkType.WiFi,
            epochMillis = 0L
        )
        val domainAddress = testAddress()
        val notificationPreferences = mockk<NotificationPreferences> {
            coEvery { shouldPostNotification(any()) } returns true
        }
        val addressNotificationService = mockk<AddressNotificationService>(relaxed = true)
        val localDataSource = mockk<AddressLocalDataSource> {
            coEvery { insertAddressIfUniqueToLast(any()) } returns entity
        }
        val addressMapper = mockk<AddressMapper> {
            coEvery { toEntity(networkAddress, any()) } returns entity
            coEvery { toDomain(entity, any()) } returns domainAddress
        }

        val useCase = ProcessAddressUseCaseImpl(
            notificationPreferences = notificationPreferences,
            addressNotificationService = addressNotificationService,
            addressLocalDataSource = localDataSource,
            addressMapper = addressMapper
        )

        useCase.process(networkAddress)

        coVerify(exactly = 1) {
            addressNotificationService.postAddress(domainAddress)
        }
    }
}
