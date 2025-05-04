package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.data.model.AddressEntity
import com.maksimowiczm.findmyip.domain.mapper.AddressMapper
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.domain.model.testAddress
import com.maksimowiczm.findmyip.domain.source.AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.source.testNetworkAddress
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class InsertNetworkAddressIfChangedUseCaseImplTest {

    @Test
    fun `Successful insertion with new ID`() = runTest {
        val networkAddress = testNetworkAddress()
        val entity = AddressEntity(
            id = 10,
            ip = networkAddress.ip,
            internetProtocol = InternetProtocol.IPv4,
            networkType = NetworkType.WiFi,
            epochMillis = 0
        )
        val address = testAddress()

        val localDataSource = mockk<AddressLocalDataSource> {
            coEvery { insertAddressIfUniqueToLast(entity) } returns entity
        }
        val mapper = mockk<AddressMapper> {
            coEvery { toEntity(networkAddress) } returns entity
            coEvery { toDomain(entity) } returns address
        }

        val useCase = InsertNetworkAddressIfChangedUseCaseImpl(
            addressLocalDataSource = localDataSource,
            addressMapper = mapper
        )

        val result = useCase.insertNetworkAddressIfChanged(networkAddress)

        assert(result != null)

        coVerify {
            localDataSource.insertAddressIfUniqueToLast(entity)
        }
    }

    @Test
    fun `No insertion with same ID`() = runTest {
        val networkAddress = testNetworkAddress()
        val entity = AddressMapper.toEntity(networkAddress)
        val localDataSource = mockk<AddressLocalDataSource> {
            coEvery { insertAddressIfUniqueToLast(entity) } returns null
        }
        val mapper = mockk<AddressMapper> {
            coEvery { toEntity(networkAddress) } returns entity
            coEvery { toDomain(entity) } returns testAddress()
        }

        val useCase = InsertNetworkAddressIfChangedUseCaseImpl(
            addressLocalDataSource = localDataSource,
            addressMapper = mapper
        )

        val result = useCase.insertNetworkAddressIfChanged(networkAddress)

        assertEquals(null, result)

        coVerify {
            localDataSource.insertAddressIfUniqueToLast(entity)
        }
    }
}
