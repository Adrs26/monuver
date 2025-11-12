package com.android.monuver.ui.feature.screen.settings

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.android.monuver.data.datastore.ThemeSetting
import com.android.monuver.ui.feature.screen.settings.export.ExportScreen
import com.android.monuver.ui.feature.utils.NavigationAnimation
import com.android.monuver.ui.navigation.Settings
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
            val isNotificationEnabled by viewModel.isNotificationEnabled.collectAsStateWithLifecycle()
            val themeSetting by viewModel.themeSetting.collectAsStateWithLifecycle()
            val isFirstBackup by viewModel.isFirstBackup.collectAsStateWithLifecycle()
            val isFirstRestore by viewModel.isFirstRestore.collectAsStateWithLifecycle()
            val isAuthenticationEnabled by viewModel.isAuthenticationEnabled.collectAsStateWithLifecycle()
            val processResult by viewModel.processResult.collectAsStateWithLifecycle()

            val settingsState = SettingsState(
                isNotificationEnabled = isNotificationEnabled,
                themeSetting = themeSetting,
                isFirstBackup = isFirstBackup,
                isFirstRestore = isFirstRestore,
                isAuthenticationEnabled = isAuthenticationEnabled,
                processResult = processResult
            )

            val settingsActions = object : SettingsActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNotificationEnableChange(isEnabled: Boolean) {
                    viewModel.setNotification(isEnabled)
                }

                override fun onThemeChange(themeSetting: ThemeSetting) {
                    viewModel.changeTheme(themeSetting)
                }

                override fun onNavigateToExport() {
                    navController.navigate(Settings.Export)
                }

                override fun onBackupData() {
                    viewModel.backupData()
                }

                override fun onSetFirstBackupToFalse() {
                    viewModel.setIsFirstBackupToFalse()
                }

                override fun onRestoreData(uri: Uri) {
                    viewModel.restoreData(uri)
                }

                override fun onSetFirstRestoreToFalse() {
                    viewModel.setIsFirstRestoreToFalse()
                }

                override fun onRemoveAllData() {
                    viewModel.deleteAllData()
                }

                override fun onAuthenticationEnableChange(isEnabled: Boolean) {
                    viewModel.setAuthentication(isEnabled)
                }
            }

            SettingsScreen(
                settingsState = settingsState,
                settingsActions = settingsActions
            )
        }
        composable<Settings.Export>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<SettingsViewModel>()
            val exportStatus by viewModel.exportStatus.collectAsStateWithLifecycle()
            val isFirstExport by viewModel.isFirstExport.collectAsStateWithLifecycle()

            ExportScreen(
                exportStatus = exportStatus,
                isFirstExport = isFirstExport,
                onSetFirstExportToFalse = viewModel::setIsFirstExportToFalse,
                onNavigateBack = navController::navigateUp,
                onExportData = viewModel::exportDataToPdf
            )
        }
    }
}