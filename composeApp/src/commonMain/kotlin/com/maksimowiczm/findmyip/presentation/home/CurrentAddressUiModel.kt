package com.maksimowiczm.findmyip.presentation.home

import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import kotlin.jvm.JvmName
import kotlinx.datetime.LocalDateTime

sealed interface CurrentAddressUiModel {
    data object Unavailable : CurrentAddressUiModel

    data class Address(
        val address: String,
        val dateTime: LocalDateTime,
        val protocolVersion: ProtocolVersion,
    ) : CurrentAddressUiModel

    companion object {
        @JvmName("fromIp4")
        fun from(addressStatus: AddressStatus<Ip4Address>) =
            when (addressStatus) {
                is AddressStatus.Error.Custom<*>,
                is AddressStatus.Error.Unknown<*> -> Unavailable

                is AddressStatus.Success<Ip4Address> ->
                    Address(
                        address = addressStatus.value.stringRepresentation(),
                        dateTime = addressStatus.dateTime,
                        protocolVersion = ProtocolVersion.IPV4,
                    )
            }

        @JvmName("fromIp6")
        fun from(addressStatus: AddressStatus<Ip6Address>) =
            when (addressStatus) {
                is AddressStatus.Error.Custom<*>,
                is AddressStatus.Error.Unknown<*> -> Unavailable

                is AddressStatus.Success<Ip6Address> ->
                    Address(
                        address = addressStatus.value.stringRepresentation(),
                        dateTime = addressStatus.dateTime,
                        protocolVersion = ProtocolVersion.IPV6,
                    )
            }
    }
}
