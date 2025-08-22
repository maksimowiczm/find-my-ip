package com.maksimowiczm.findmyip.infrastructure.fake

// 22.08.2025 - local `dig` command
object CommonAddresses {
    const val GOOGLE_V4 = "142.250.203.142"
    const val GITHUB_V4 = "140.82.121.4"
    const val CHATGPT_V4 = "104.18.32.47"

    val v4 = listOf(GOOGLE_V4, GITHUB_V4, CHATGPT_V4)

    const val GOOGLE_V6 = "2a00:1450:401b:80d::200e"
    const val CHATGPT_V6 = "2606:4700:4400::6812:202f"
    const val ANDROID_V6 = "2a00:1450:401b:80e::2004"

    val v6 = listOf(GOOGLE_V6, CHATGPT_V6, ANDROID_V6)
}
