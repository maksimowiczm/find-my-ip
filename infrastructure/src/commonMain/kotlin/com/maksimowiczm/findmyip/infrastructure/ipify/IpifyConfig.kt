package com.maksimowiczm.findmyip.infrastructure.ipify

interface IpifyConfig {
    val ip4Url: String
    val ip6Url: String
}

internal object IpifyConfigImpl : IpifyConfig {
    override val ip4Url: String = "https://api.ipify.org"
    override val ip6Url: String = "https://api6.ipify.org"
}
