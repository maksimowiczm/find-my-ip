package com.maksimowiczm.findmyip.domain.mapper

import com.maksimowiczm.findmyip.data.model.AddressEntity
import com.maksimowiczm.findmyip.domain.model.Address
import com.maksimowiczm.findmyip.domain.model.AddressId
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.domain.source.NetworkAddress
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import org.junit.Assert
import org.junit.Test

class AddressMapperTest {

    private val mapper
        get() = AddressMapper

    @Test
    fun `toDomain valid conversion`() {
        val addressEntity = AddressEntity(
            id = 1,
            ip = "127.0.0.1",
            internetProtocol = InternetProtocol.IPv4,
            networkType = NetworkType.WiFi,
            epochMillis = 1672531200000
        )

        val result = mapper.toDomain(
            addressEntity = addressEntity,
            zone = TimeZone.UTC
        )

        Assert.assertEquals(AddressId(1), result.id)
        Assert.assertEquals("127.0.0.1", result.ip)
        Assert.assertEquals(InternetProtocol.IPv4, result.internetProtocol)
        Assert.assertEquals(NetworkType.WiFi, result.networkType)
        Assert.assertEquals(LocalDateTime(2023, 1, 1, 0, 0, 0), result.dateTime)
    }

    @Test
    fun `toEntity valid conversion`() {
        val address = Address(
            id = AddressId(1),
            ip = "127.0.0.1",
            internetProtocol = InternetProtocol.IPv4,
            networkType = NetworkType.WiFi,
            dateTime = LocalDateTime(2023, 1, 1, 0, 0, 0)
        )

        val result = mapper.toEntity(
            address = address,
            zone = TimeZone.UTC
        )

        Assert.assertEquals(1L, result.id)
        Assert.assertEquals("127.0.0.1", result.ip)
        Assert.assertEquals(InternetProtocol.IPv4, result.internetProtocol)
        Assert.assertEquals(NetworkType.WiFi, result.networkType)
        Assert.assertEquals(1672531200000L, result.epochMillis)
    }

    @Test
    fun `NetworkAddress toEntity valid conversion`() {
        val address = NetworkAddress(
            ip = "127.0.0.1",
            networkType = NetworkType.WiFi,
            internetProtocol = InternetProtocol.IPv4,
            dateTime = LocalDateTime(2023, 1, 1, 0, 0, 0)
        )

        val result = mapper.toEntity(
            address = address,
            zone = TimeZone.UTC
        )

        Assert.assertEquals("127.0.0.1", result.ip)
        Assert.assertEquals(InternetProtocol.IPv4, result.internetProtocol)
        Assert.assertEquals(NetworkType.WiFi, result.networkType)
        Assert.assertEquals(1672531200000L, result.epochMillis)
    }
}
