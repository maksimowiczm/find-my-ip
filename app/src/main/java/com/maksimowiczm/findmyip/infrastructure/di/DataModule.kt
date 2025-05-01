package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.network.KtorAddressObserver
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.infrastructure.IpifyConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.onClose

val dataModule = module {
    single(named("ktorClient")) {
        HttpClient(OkHttp) {
            install(HttpTimeout) {
                connectTimeoutMillis = 5_000
                requestTimeoutMillis = 5_000
                socketTimeoutMillis = 5_000
            }
        }
    }.onClose {
        it?.close()
    }

    single(named(InternetProtocol.IPv4)) {
        KtorAddressObserver(
            url = IpifyConfig.IPV4,
            client = get(named("ktorClient"))
        )
    }.bind<AddressObserver>()

    single(named(InternetProtocol.IPv6)) {
        KtorAddressObserver(
            url = IpifyConfig.IPV6,
            client = get(named("ktorClient"))
        )
    }.bind<AddressObserver>()
}
