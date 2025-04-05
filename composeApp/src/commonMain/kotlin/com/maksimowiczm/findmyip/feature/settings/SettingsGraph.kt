package com.maksimowiczm.findmyip.feature.settings

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.maksimowiczm.findmyip.feature.settings.global.SettingsScreen
import com.maksimowiczm.findmyip.feature.settings.history.HistorySettingsScreen
import com.maksimowiczm.findmyip.feature.settings.internetprotocol.InternetProtocolSettingsScreen
import com.maksimowiczm.findmyip.feature.settings.language.LanguageSettingsScreen
import com.maksimowiczm.findmyip.navigation.ForwardBackwardComposableDefaults
import kotlinx.serialization.Serializable

@Serializable
data object GlobalSettings

@Serializable
data object HistorySettings

@Serializable
data object InternetProtocolSettings

@Serializable
data object LanguageSettings

inline fun <reified T : Any> NavGraphBuilder.settingsGraph(
    navController: NavController,
    noinline enterTransition:
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    noinline exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition,
    noinline popEnterTransition:
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = enterTransition,
    noinline popExitTransition:
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = exitTransition
) {
    val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        if (initialState.destination.hasRoute<GlobalSettings>()) {
            ForwardBackwardComposableDefaults.enterTransition()
        } else {
            enterTransition()
        }
    }

    val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        if (
            targetState.destination.hasRoute<GlobalSettings>() ||
            targetState.destination.hasRoute<HistorySettings>() ||
            targetState.destination.hasRoute<InternetProtocolSettings>() ||
            targetState.destination.hasRoute<LanguageSettings>()
        ) {
            ForwardBackwardComposableDefaults.exitTransition()
        } else {
            exitTransition()
        }
    }

    val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        if (
            initialState.destination.hasRoute<GlobalSettings>() ||
            initialState.destination.hasRoute<HistorySettings>() ||
            initialState.destination.hasRoute<InternetProtocolSettings>() ||
            initialState.destination.hasRoute<LanguageSettings>()
        ) {
            ForwardBackwardComposableDefaults.popEnterTransition()
        } else {
            popEnterTransition()
        }
    }

    val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        if (
            targetState.destination.hasRoute<GlobalSettings>() ||
            targetState.destination.hasRoute<HistorySettings>() ||
            targetState.destination.hasRoute<InternetProtocolSettings>() ||
            targetState.destination.hasRoute<LanguageSettings>()
        ) {
            ForwardBackwardComposableDefaults.popExitTransition()
        } else {
            popExitTransition()
        }
    }

    navigation<T>(
        startDestination = GlobalSettings
    ) {
        composable<GlobalSettings>(
            enterTransition = enter,
            exitTransition = exit,
            popEnterTransition = popEnter,
            popExitTransition = popExit
        ) {
            SettingsScreen(
                onHistorySettings = {
                    navController.navigate(HistorySettings) {
                        launchSingleTop = true
                    }
                },
                onInternetProtocolSettings = {
                    navController.navigate(InternetProtocolSettings) {
                        launchSingleTop = true
                    }
                },
                onLanguageSettings = {
                    navController.navigate(LanguageSettings) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<HistorySettings>(
            enterTransition = enter,
            exitTransition = exit,
            popEnterTransition = popEnter,
            popExitTransition = popExit
        ) {
            HistorySettingsScreen()
        }
        composable<InternetProtocolSettings>(
            enterTransition = enter,
            exitTransition = exit,
            popEnterTransition = popEnter,
            popExitTransition = popExit
        ) {
            InternetProtocolSettingsScreen()
        }
        composable<LanguageSettings>(
            enterTransition = enter,
            exitTransition = exit,
            popEnterTransition = popEnter,
            popExitTransition = popExit
        ) {
            LanguageSettingsScreen()
        }
    }
}
