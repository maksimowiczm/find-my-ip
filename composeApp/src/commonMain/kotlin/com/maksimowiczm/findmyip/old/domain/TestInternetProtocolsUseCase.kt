package com.maksimowiczm.findmyip.old.domain

import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.old.data.Keys
import com.maksimowiczm.findmyip.old.data.PublicAddressRepository
import com.maksimowiczm.findmyip.old.data.UserPreferencesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * This use case is responsible for testing and enabling IPv4 and IPv6 features. It works only once
 * unless [Keys.ip_features_tested] is reset.
 */
class TestInternetProtocolsUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val publicAddressRepository: PublicAddressRepository
) {
    suspend operator fun invoke() {
        val tested = userPreferencesRepository.get(Keys.ip_features_tested)

        if (tested == true) {
            return
        }

        coroutineScope {
            val (ipv4test, ipv6test) = awaitAll(
                async {
                    publicAddressRepository.refreshCurrentAddress(InternetProtocolVersion.IPv4)
                },
                async {
                    publicAddressRepository.refreshCurrentAddress(InternetProtocolVersion.IPv6)
                }
            )

            // If both test fail set IPv4 as default.
            val ipv4 = if (ipv4test.isErr && ipv6test.isErr) {
                true
            } else {
                ipv4test.isOk
            }
            val ipv6 = ipv6test.isOk

            userPreferencesRepository.set(
                Keys.ipv4_enabled to ipv4,
                Keys.ipv6_enabled to ipv6,
                Keys.ip_features_tested to true
            )
        }
    }
}
