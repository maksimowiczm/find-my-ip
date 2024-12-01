package com.maksimowiczm.findmyip.domain

import com.maksimowiczm.findmyip.data.Keys
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.repository.PublicAddressRepository
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

/**
 * This use case is responsible for testing and enabling IPv4 and IPv6 features. It works only once
 * unless [Keys.ip_features_tested] is reset.
 */
class TestInternetProtocolsUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val publicAddressRepository: PublicAddressRepository
) {
    suspend operator fun invoke() {
        val tested = userPreferencesRepository.get(Keys.ip_features_tested).first()

        if (tested == true) {
            return
        }

        coroutineScope {
            val (ipv4test, ipv6test) = awaitAll(
                async {
                    publicAddressRepository.refreshCurrentAddress(
                        InternetProtocolVersion.IPv4
                    )
                },
                async {
                    publicAddressRepository.refreshCurrentAddress(
                        InternetProtocolVersion.IPv6
                    )
                }
            )

            userPreferencesRepository.set(Keys.ipv4_enabled, ipv4test.isOk)
            userPreferencesRepository.set(Keys.ipv6_enabled, ipv6test.isOk)

            // If both tests failed, enable IPv4 by default.
            if (ipv4test.isErr && ipv6test.isErr) {
                userPreferencesRepository.set(Keys.ipv4_enabled, true)
            }

            userPreferencesRepository.set(Keys.ip_features_tested, true)
        }
    }
}
