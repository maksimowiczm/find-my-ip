@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.ui.settings

import androidx.compose.foundation.lazy.LazyListScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

expect fun buildPlatformSettings(navController: NavController): LazyListScope.() -> Unit

expect fun NavGraphBuilder.platformSettingsGraph(navController: NavController)
