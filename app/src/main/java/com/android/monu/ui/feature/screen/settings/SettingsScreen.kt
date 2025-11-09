package com.android.monu.ui.feature.screen.settings

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import com.android.monu.R
import com.android.monu.data.datastore.ThemeSetting
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.components.ConfirmationDialog
import com.android.monu.ui.feature.screen.settings.components.FirstActionConfirmation
import com.android.monu.ui.feature.screen.settings.components.SettingsApplicationData
import com.android.monu.ui.feature.screen.settings.components.SettingsPreference
import com.android.monu.ui.feature.screen.settings.components.SettingsSecurity
import com.android.monu.ui.feature.screen.settings.components.SettingsThemeDialog
import com.android.monu.ui.feature.utils.AuthenticationManager
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.android.monu.ui.feature.utils.showToast
import com.android.monu.ui.worker.startReminderWorker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    settingsState: SettingsState,
    settingsActions: SettingsActions
) {
    var showThemeDialog by remember { mutableStateOf(false) }
    var showFirstBackupDialog by remember { mutableStateOf(false) }
    var showFirstRestoreDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAuthenticationPrompt by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = LocalActivity.current as FragmentActivity

    val notificationsPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = { isGranted ->
                if (isGranted) {
                    startReminderWorker(context)
                } else {
                    context.getString(
                        R.string.notification_permissions_required_to_use_bill_reminder_feature
                    ).showMessageWithToast(context)
                }
            }
        )
    } else { null }

    val storagePermissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
        onPermissionResult = { isGranted ->
            if (isGranted) {
                settingsActions.onBackupData()
            } else {
                context.getString(R.string.write_permission_storage_is_required_to_backup_data)
                    .showMessageWithToast(context)
            }
        }
    )
    val pickJsonFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { settingsActions.onRestoreData(it) }
    }

    LaunchedEffect(notificationsPermissionState?.status?.isGranted) {
        settingsActions.onNotificationEnableChange(
            notificationsPermissionState?.status?.isGranted == true
        )
    }

    LaunchedEffect(settingsState.processResult) {
        settingsState.processResult?.showToast(context)
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.settings),
                onNavigateBack = settingsActions::onNavigateBack
            ) 
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsPreference(
                isNotificationEnabled = settingsState.isNotificationEnabled,
                themeSetting = settingsState.themeSetting,
                onNotificationEnableChange = {
                    if (notificationsPermissionState?.status?.isGranted == false) {
                        notificationsPermissionState.launchPermissionRequest()
                    } else {
                        openNotificationSettings(context)
                    }
                },
                onThemeChange = { showThemeDialog = true },
                modifier = Modifier.padding(top = 8.dp)
            )
            SettingsApplicationData(
                onExportDataClicked = settingsActions::onNavigateToExport,
                onBackupDataClicked = {
                    if (settingsState.isFirstBackup) {
                        showFirstBackupDialog = true
                    } else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                            !storagePermissionState.status.isGranted) {
                            storagePermissionState.launchPermissionRequest()
                        } else {
                            settingsActions.onBackupData()
                        }
                    }
                },
                onRestoreDataClicked = {
                    if (settingsState.isFirstRestore) {
                        showFirstRestoreDialog = true
                    } else {
                        pickJsonFileLauncher.launch(arrayOf("application/json"))
                    }
                },
                onDeleteDataClicked = { showDeleteDialog = true }
            )
            SettingsSecurity(
                isAuthenticationEnabled = settingsState.isAuthenticationEnabled,
                onAuthenticationClicked = { showAuthenticationPrompt = true }
            )
        }
    }

    if (showThemeDialog) {
        SettingsThemeDialog(
            themeSetting = settingsState.themeSetting,
            onThemeChange = settingsActions::onThemeChange,
            onDismissRequest = { showThemeDialog = false }
        )
    }

    if (showFirstBackupDialog) {
        FirstActionConfirmation(
            text = stringResource(R.string.first_backup_confirmation),
            onDismissRequest = {
                showFirstBackupDialog = false
                settingsActions.onSetFirstBackupToFalse()
            },
            onConfirmRequest = {
                showFirstBackupDialog = false
                settingsActions.onSetFirstBackupToFalse()
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                    !storagePermissionState.status.isGranted) {
                    storagePermissionState.launchPermissionRequest()
                } else {
                    settingsActions.onBackupData()
                }
            }
        )
    }

    if (showFirstRestoreDialog) {
        FirstActionConfirmation(
            text = stringResource(R.string.first_restore_confirmation),
            onDismissRequest = {
                showFirstRestoreDialog = false
                settingsActions.onSetFirstRestoreToFalse()
            },
            onConfirmRequest = {
                showFirstRestoreDialog = false
                settingsActions.onSetFirstRestoreToFalse()
                pickJsonFileLauncher.launch(arrayOf("application/json"))
            }
        )
    }

    if (showDeleteDialog) {
        ConfirmationDialog(
            text = stringResource(R.string.delete_all_application_data_confirmation),
            onDismissRequest = { showDeleteDialog = false },
            onConfirmRequest = {
                showDeleteDialog = false
                settingsActions.onRemoveAllData()
                context.getString(R.string.all_data_successfully_deleted).showMessageWithToast(context)
            }
        )
    }

    if (showAuthenticationPrompt && AuthenticationManager.canAuthenticate(context)) {
        AuthenticationManager.showBiometricPrompt(
            activity = activity,
            onAuthSuccess = {
                showAuthenticationPrompt = false
                settingsActions.onAuthenticationEnableChange(!settingsState.isAuthenticationEnabled)
            },
            onAuthFailed = {
                showAuthenticationPrompt = false
                context.getString(R.string.fingerprint_not_matched).showMessageWithToast(context)
            },
            onAuthError = { showAuthenticationPrompt = false }
        )
    }
}

private fun openNotificationSettings(context: Context) {
    val intent = Intent().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        } else {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = "package:${context.packageName}".toUri()
        }
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}

data class SettingsState(
    val isNotificationEnabled: Boolean,
    val themeSetting: ThemeSetting,
    val isFirstBackup: Boolean,
    val isFirstRestore: Boolean,
    val isAuthenticationEnabled: Boolean,
    val processResult: DatabaseResultState?,
)

interface SettingsActions {
    fun onNavigateBack()
    fun onNotificationEnableChange(isEnabled: Boolean)
    fun onThemeChange(themeSetting: ThemeSetting)
    fun onNavigateToExport()
    fun onBackupData()
    fun onSetFirstBackupToFalse()
    fun onRestoreData(uri: Uri)
    fun onSetFirstRestoreToFalse()
    fun onRemoveAllData()
    fun onAuthenticationEnableChange(isEnabled: Boolean)
}