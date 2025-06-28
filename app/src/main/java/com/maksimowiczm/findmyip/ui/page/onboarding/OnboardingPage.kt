package com.maksimowiczm.findmyip.ui.page.onboarding

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.navigation.ForwardBackwardComposableDefaults
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingPage(
    modifier: Modifier = Modifier,
    viewModel: OnboardingPageViewModel = koinViewModel()
) {
    OnboardingPage(
        onEnableNotifications = viewModel::enableNotifications,
        onSkipNotifications = viewModel::skipNotifications,
        onEnableBackgroundTracking = viewModel::enableBackgroundTracking,
        onSkipBackgroundTracking = viewModel::skipBackgroundTracking,
        onFinish = viewModel::onFinish,
        modifier = modifier
    )
}

private enum class Screens {
    BeforeYouGetStarted,
    BackgroundTracker,
    Notifications
}

@Composable
fun OnboardingPage(
    onEnableNotifications: () -> Unit,
    onSkipNotifications: () -> Unit,
    onEnableBackgroundTracking: () -> Unit,
    onSkipBackgroundTracking: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Surface(modifier) {
        NavHost(
            navController = navController,
            startDestination = Screens.BeforeYouGetStarted.name
        ) {
            onboardingComposable(Screens.BeforeYouGetStarted.name) {
                BeforeYouGetStartedPage(
                    onAgree = {
                        navController.navigate(Screens.BackgroundTracker.name)
                    }
                )
            }
            onboardingComposable(Screens.BackgroundTracker.name) {
                KeepTrackingPage(
                    onEnable = {
                        onEnableBackgroundTracking()
                        navController.navigate(Screens.Notifications.name)
                    },
                    onSkip = {
                        onSkipBackgroundTracking()
                        navController.navigate(Screens.Notifications.name)
                    }
                )
            }
            onboardingComposable(Screens.Notifications.name) {
                StayInformedPage(
                    onEnable = {
                        onEnableNotifications()
                        onFinish()
                    },
                    onSkip = {
                        onSkipNotifications()
                        onFinish()
                    }
                )
            }
        }
    }
}

private fun NavGraphBuilder.onboardingComposable(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = { ForwardBackwardComposableDefaults.enterTransition() },
        exitTransition = { ForwardBackwardComposableDefaults.exitTransition() },
        popEnterTransition = { ForwardBackwardComposableDefaults.popEnterTransition() },
        popExitTransition = { ForwardBackwardComposableDefaults.popExitTransition() },
        content = content
    )
}
