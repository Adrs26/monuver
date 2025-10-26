package com.android.monu.ui.feature.screen.settings

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.android.monu.ui.feature.screen.settings.export.ExportScreen
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
            val isFirstBackup by viewModel.isFirstBackup.collectAsStateWithLifecycle()
            val isFirstRestore by viewModel.isFirstRestore.collectAsStateWithLifecycle()
            val processResult by viewModel.processResult.collectAsStateWithLifecycle()

            SettingsScreen(
                themeSetting = themeSetting,
                isFirstBackup = isFirstBackup,
                isFirstRestore = isFirstRestore,
                processResult = processResult,
                onThemeChange = viewModel::changeTheme,
                onNavigateBack = navController::navigateUp,
                onNavigateToExport = { navController.navigate(Settings.Export) },
                onBackupData = viewModel::backupData,
                onSetFirstBackupToFalse = viewModel::setIsFirstBackupToFalse,
                onRestoreData = viewModel::restoreData,
                onSetFirstRestoreToFalse = viewModel::setIsFirstRestoreToFalse,
                onRemoveAllData = viewModel::deleteAllData
            )
        }
        composable<Settings.Export>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<SettingsViewModel>()
            val exportProgress by viewModel.exportProgress.collectAsStateWithLifecycle()
            val isFirstExport by viewModel.isFirstExport.collectAsStateWithLifecycle()

            ExportScreen(
                exportProgress = exportProgress,
                isFirstExport = isFirstExport,
                onSetFirstExportToFalse = viewModel::setIsFirstExportToFalse,
                onNavigateBack = navController::navigateUp,
                onExportData = viewModel::exportDataToPdf
            )
        }
    }
}