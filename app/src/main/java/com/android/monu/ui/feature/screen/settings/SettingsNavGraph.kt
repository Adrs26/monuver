package com.android.monu.ui.feature.screen.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.navigation.MainSettings
import com.android.monu.ui.navigation.Settings

fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController
) {
    navigation<Settings>(startDestination = MainSettings) {
        composable<MainSettings>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            SettingsScreen(
                onNavigateBack = navController::navigateUp
            )
        }
    }
}