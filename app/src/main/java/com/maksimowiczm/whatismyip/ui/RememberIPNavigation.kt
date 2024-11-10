package com.maksimowiczm.whatismyip.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.whatismyip.current_address.CurrentAddressScreen
import com.maksimowiczm.whatismyip.domain.ObserveCurrentAddressUseCase
import com.maksimowiczm.whatismyip.current_address.CurrentAddressViewModel
import com.maksimowiczm.whatismyip.data.repository.PublicAddressRepository
import com.maksimowiczm.whatismyip.data.network.PublicAddressDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

@Composable
fun RememberIPNavigation() {
    val navController = rememberNavController()

    val publicAddressRepository = PublicAddressRepository(PublicAddressDataSource(Dispatchers.IO))

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        composable<HomeRoute> {
            CurrentAddressScreen(
                currentAddressViewModel = CurrentAddressViewModel(
                    publicAddressRepository,
                    ObserveCurrentAddressUseCase(publicAddressRepository)
                )
            )
        }
    }
}
