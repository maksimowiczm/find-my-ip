package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.SQLiteStatement
import androidx.sqlite.execSQL
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractFindMyIP5MigrationTest {
    abstract fun getTestHelper(): MigrationTestHelper

    open fun migrate() {
        val helper = getTestHelper()

        helper.createDatabase(3).use {
            it.execSQL(
                """
                INSERT INTO Address (Id, Ip, InternetProtocol, NetworkType, EpochMillis) VALUES
                (1, '192.168.1.100', 0, 'cellular', 1693747200000),
                (2, '2001:db8::1', 1, 'vpn', 1693833600000),
                (3, '10.0.0.50', 0, 'wifi', 1693920000000),
                (4, '203.0.113.42', 0, 'wifi', 1694006400000),
                (5, 'fe80::1', 1, 'wifi', 1694092800000)
                """
                    .trimIndent()
            )
        }

        helper.runMigrationsAndValidate(4, listOf(FindMyIP5Migration)).use { conn ->
            conn
                .prepare(
                    """
                    SELECT id, address, domain, addressVersion, networkType, epochSeconds
                    FROM AddressHistory
                    ORDER BY id
                """
                        .trimIndent()
                )
                .use {
                    it.validateAddressHistoryRow(
                        expectedId = 1L,
                        expectedAddress = "192.168.1.100",
                        expectedDomain = null,
                        expectedAddressVersion = 4,
                        expectedNetworkType = "cellular",
                        expectedEpochSeconds = 1693747200L,
                    )
                    it.validateAddressHistoryRow(
                        expectedId = 2L,
                        expectedAddress = "2001:db8::1",
                        expectedDomain = null,
                        expectedAddressVersion = 6,
                        expectedNetworkType = "vpn",
                        expectedEpochSeconds = 1693833600L,
                    )
                    it.validateAddressHistoryRow(
                        expectedId = 3L,
                        expectedAddress = "10.0.0.50",
                        expectedDomain = null,
                        expectedAddressVersion = 4,
                        expectedNetworkType = "wifi",
                        expectedEpochSeconds = 1693920000L,
                    )
                    it.validateAddressHistoryRow(
                        expectedId = 4L,
                        expectedAddress = "203.0.113.42",
                        expectedDomain = null,
                        expectedAddressVersion = 4,
                        expectedNetworkType = "wifi",
                        expectedEpochSeconds = 1694006400L,
                    )
                    it.validateAddressHistoryRow(
                        expectedId = 5L,
                        expectedAddress = "fe80::1",
                        expectedDomain = null,
                        expectedAddressVersion = 6,
                        expectedNetworkType = "wifi",
                        expectedEpochSeconds = 1694092800L,
                    )
                    assertTrue(!it.step(), "No more rows should be present")
                }
        }
    }
}

private fun SQLiteStatement.validateAddressHistoryRow(
    expectedId: Long,
    expectedAddress: String,
    expectedDomain: String?,
    expectedAddressVersion: Int,
    expectedNetworkType: String,
    expectedEpochSeconds: Long,
) {
    assertTrue("Row expected") { step() }
    assertEquals(expectedId, getLong(0), "ID should match")
    assertEquals(expectedAddress, getText(1), "Address should match")
    if (expectedDomain == null) {
        assertTrue(isNull(2), "Domain should be NULL")
    } else {
        assertEquals(expectedDomain, getText(2), "Domain should match")
    }
    assertEquals(expectedAddressVersion, getInt(3), "AddressVersion should match")
    assertEquals(expectedNetworkType, getText(4), "NetworkType should match")
    assertEquals(expectedEpochSeconds, getLong(5), "EpochSeconds should match")
}

expect class FindMyIP5MigrationTest : AbstractFindMyIP5MigrationTest {
    override fun getTestHelper(): MigrationTestHelper

    override fun migrate()
}
