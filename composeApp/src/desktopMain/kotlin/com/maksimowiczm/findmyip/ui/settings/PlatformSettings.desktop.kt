package com.maksimowiczm.findmyip.ui.settings

import androidx.compose.foundation.lazy.LazyListScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

actual fun buildPlatformSettings(navController: NavController): LazyListScope.() -> Unit = {}

actual fun NavGraphBuilder.platformSettingsGraph(navController: NavController) = Unit
