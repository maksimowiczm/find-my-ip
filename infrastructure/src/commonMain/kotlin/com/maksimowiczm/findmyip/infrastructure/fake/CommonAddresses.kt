package com.maksimowiczm.findmyip.infrastructure.fake

// 22.08.2025 - local `dig` command
object CommonAddresses {
    /** google.com */
    const val GOOGLE_V4_1 = "142.250.203.142"

    /** www.google.com */
    const val GOOGLE_V4_2 = "216.58.208.196"

    /** github.com */
    const val GITHUB_V4_1 = "140.82.121.4"
    const val GITHUB_V4_2 = "140.82.121.3"

    /** chatgpt.com */
    const val CHATGPT_V4_1 = "104.18.32.47"
    const val CHATGPT_V4_2 = "172.64.155.209"

    val v4 = listOf(GOOGLE_V4_1, GOOGLE_V4_2, GITHUB_V4_1, GITHUB_V4_2, CHATGPT_V4_1, CHATGPT_V4_2)

    const val GOOGLE_V6 = "2a00:1450:401b:80d::200e"
    const val CHATGPT_V6 = "2606:4700:4400::6812:202f"
    const val ANDROID_V6 = "2a00:1450:401b:80e::2004"

    val v6 = listOf(GOOGLE_V6, CHATGPT_V6, ANDROID_V6)
}
