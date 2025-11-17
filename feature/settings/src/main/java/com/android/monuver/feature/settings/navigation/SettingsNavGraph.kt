package com.android.monuver.feature.settings.navigation

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.android.monuver.core.domain.common.ThemeState
import com.android.monuver.core.presentation.navigation.Settings
import com.android.monuver.core.presentation.util.NavigationAnimation
import com.android.monuver.feature.settings.presentation.SettingsActions
import com.android.monuver.feature.settings.presentation.SettingsScreen
import com.android.monuver.feature.settings.presentation.SettingsState
import com.android.monuver.feature.settings.presentation.SettingsViewModel
import com.android.monuver.feature.settings.presentation.export.ExportScreen
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
            val themeState by viewModel.themeState.collectAsStateWithLifecycle()
            val isFirstBackup by viewModel.isFirstBackup.collectAsStateWithLifecycle()
            val isFirstRestore by viewModel.isFirstRestore.collectAsStateWithLifecycle()
            val isAuthenticationEnabled by viewModel.isAuthenticationEnabled.collectAsStateWithLifecycle()
            val processResult by viewModel.processResult.collectAsStateWithLifecycle()

            val settingsState = SettingsState(
                isNotificationEnabled = isNotificationEnabled,
                themeState = themeState,
                isFirstBackup = isFirstBackup,
                isFirstRestore = isFirstRestore,
                isAuthenticationEnabled = isAuthenticationEnabled,
                result = processResult
            )

            val settingsActions = object : SettingsActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNotificationEnableChange(isEnabled: Boolean) {
                    viewModel.setNotification(isEnabled)
                }

                override fun onThemeChange(themeState: ThemeState) {
                    viewModel.changeTheme(themeState)
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