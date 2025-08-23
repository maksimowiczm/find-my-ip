package com.maksimowiczm.findmyip.feature.home.presentation

import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import kotlin.jvm.JvmName
import kotlinx.datetime.LocalDateTime

internal sealed interface CurrentAddressUiModel {
    data object Unavailable : CurrentAddressUiModel

    data class Address(
        override val address: String,
        override val domain: String?,
        override val dateTime: LocalDateTime,
        override val internetProtocolVersion: InternetProtocolVersion,
        override val networkType: NetworkType,
    ) : CurrentAddressUiModel, AddressUiModel

    companion object {
        @JvmName("fromIp4")
        fun from(addressStatus: AddressStatus<Ip4Address>) =
            when (addressStatus) {
                is AddressStatus.Error.Custom<*>,
                is AddressStatus.Error.Unknown<*> -> Unavailable

                is AddressStatus.Success<Ip4Address> ->
                    Address(
                        address = addressStatus.address.stringRepresentation(),
                        domain = addressStatus.domain,
                        dateTime = addressStatus.dateTime,
                        internetProtocolVersion = InternetProtocolVersion.IPV4,
                        networkType = NetworkType.fromDomain(addressStatus.networkType),
                    )
            }

        @JvmName("fromIp6")
        fun from(addressStatus: AddressStatus<Ip6Address>) =
            when (addressStatus) {
                is AddressStatus.Error.Custom<*>,
                is AddressStatus.Error.Unknown<*> -> Unavailable

                is AddressStatus.Success<Ip6Address> ->
                    Address(
                        address = addressStatus.address.stringRepresentation(),
                        domain = addressStatus.domain,
                        dateTime = addressStatus.dateTime,
                        internetProtocolVersion = InternetProtocolVersion.IPV6,
                        networkType = NetworkType.fromDomain(addressStatus.networkType),
                    )
            }
    }
}
