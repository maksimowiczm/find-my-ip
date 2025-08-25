package com.maksimowiczm.findmyip.domain

interface SponsorshipMethod {

    /** Represents a sponsorship method that can be used to support the application. */
    val name: String

    /**
     * Indicates whether this sponsorship method is the primary one. The primary method is typically
     * the most preferred or recommended way to support the application.
     */
    val primary: Boolean

    /** Represents a sponsorship method that redirects to a specific URL. */
    data class Url(
        override val name: String,
        val url: String,
        val displayText: String,
        override val primary: Boolean = false,
    ) : SponsorshipMethod

    /** Represents a sponsorship method that involves cryptocurrency addresses. */
    data class Crypto(
        override val name: String,
        val address: String,
        override val primary: Boolean = false,
    ) : SponsorshipMethod

    companion object {
        val methods =
            listOf(
                Url(
                    "Ko-fi",
                    primary = true,
                    url = "https://ko-fi.com/maksimowiczm/5",
                    displayText = "ko-fi.com/maksimowiczm",
                ),
                Crypto("Bitcoin", "bc1qml4g4jwt6mqq2tsk9u7udhwysmjfknx68taln2"),
                Crypto(
                    "Monero",
                    "41tP8QxdL5hduxcntGwJD92GJDdCTKDyyGSKofbgdgaLG2uJuqgK7daYymBQuJ1iA48LuiLdfoduFMLk1kdkTRKSC4mHkMY",
                ),
                Crypto("Ethereum", "0x7C794aF78235504014cC5c987161b80a803ee514"),
                Crypto("Solana", "6kdSsE5xQBmiQ5DY5bqWJX8jK2fuWiGHx9YgeAvFU4gq"),
                Crypto("Litecoin", "ltc1qrjpk7p4nzzm86lrfue2kz4ln4l6fjreha9lrvw"),
                Crypto(
                    "Zcash",
                    "u1hd2wvlp3qwgj2p68cz2cl3zajyjcruz6hhxmhmlrwq53n3sz32xtngjrrg2phtgzwlam370w3yjuf37k797y3w8tc4mc5lhs7nedxq2yze8kk44xr2tmnlzej0dw3u5lry4alvftejlf2qtz3r38gxyyvg54adkvncn0w7dhelt98letfjh2x5sda8ay50cnsupeg4jzpjy22dnmtsu",
                ),
                Crypto("Dash", "XqDN5Yowv5r9Wpduh1k5LtFkidmBYgSGkQ"),
                Crypto("Avalanche", "0x7C794aF78235504014cC5c987161b80a803ee514"),
            )
    }
}
