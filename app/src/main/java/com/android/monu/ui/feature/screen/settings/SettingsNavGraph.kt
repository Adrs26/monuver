package com.android.monu.ui.feature.screen.settings

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.navigation.Settings
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController
) {
    navigation<Settings>(startDestination = Settings.Main) {
        composable<Settings.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<SettingsViewModel>()
            val themeSetting by viewModel.themeSetting.collectAsStateWithLifecycle()

            SettingsScreen(
                themeSetting = themeSetting,
                onThemeChange = viewModel::changeTheme,
                onNavigateBack = navController::navigateUp,
                onRemoveAllData = viewModel::deleteAllData
            )
        }
    }
}