package com.maksimowiczm.findmyip.ui.page.settings.sponsor

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.maksimowiczm.findmyip.R

interface CryptoSponsorMethod {
    val name: String
    val address: String

    @Composable
    fun Icon(modifier: Modifier = Modifier)

    companion object {
        val all = listOf(
            bitcoin,
            monero
        )
    }
}

private val bitcoin = object : CryptoSponsorMethod {
    override val name = "Bitcoin"
    override val address = "bc1qca6my0xj8g0t6atmfsla0z66qhx66sd6dzg8hk"

    @Composable
    override fun Icon(modifier: Modifier) {
        Image(
            painter = painterResource(R.drawable.bitcoin_logo),
            contentDescription = null,
            modifier = modifier
        )
    }
}

private val monero = object : CryptoSponsorMethod {
    override val name = "Monero"
    override val address =
        "43jDszSMNqKjLxEoovQo8H8WMExkBwZveR9BqF9LoRhugHtpq4heNGXRK3Fe1ijR5LFGbiEGCw6HRTk3Q1YN2e2H4kSbNiv"

    @Composable
    override fun Icon(modifier: Modifier) {
        Image(
            painter = painterResource(R.drawable.monero_logo),
            contentDescription = null,
            modifier = modifier
        )
    }
}
