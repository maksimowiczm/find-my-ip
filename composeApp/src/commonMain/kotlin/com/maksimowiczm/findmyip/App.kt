package com.maksimowiczm.findmyip

import androidx.compose.runtime.Composable
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.presentation.currentaddress.CurrentAddressViewModel
import com.maksimowiczm.findmyip.ui.currentaddress.CurrentAddressRoute
import com.maksimowiczm.findmyip.ui.shared.FindMyIpTheme
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalUnsignedTypes::class)
@Composable
fun App() {
    FindMyIpTheme {
        CurrentAddressRoute(
            viewModel =
                CurrentAddressViewModel(
                    observeCurrentIp4AddressUseCase = {
                        flow {
                            emit(AddressStatus.Success(Ip4Address(ubyteArrayOf(127u, 0u, 0u, 1u))))
                        }
                    }
                )
        )
    }
}
