package com.android.monuver.feature.settings.presentation

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
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.common.ThemeState
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.ConfirmationDialog
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.core.presentation.util.AuthenticationManager
import com.android.monuver.feature.settings.R
import com.android.monuver.feature.settings.presentation.components.SettingsApplicationData
import com.android.monuver.feature.settings.presentation.components.SettingsFirstActionConfirmationDialog
import com.android.monuver.feature.settings.presentation.components.SettingsPreference
import com.android.monuver.feature.settings.presentation.components.SettingsSecurity
import com.android.monuver.feature.settings.presentation.components.SettingsThemeDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun SettingsScreen(
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
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
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

    LaunchedEffect(settingsState.result) {
        settingsState.result?.showToast(context)
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
                themeState = settingsState.themeState,
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
                onExportData = settingsActions::onNavigateToExport,
                onBackupData = {
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
                onRestoreData = {
                    if (settingsState.isFirstRestore) {
                        showFirstRestoreDialog = true
                    } else {
                        pickJsonFileLauncher.launch(arrayOf("application/json"))
                    }
                },
                onDeleteData = { showDeleteDialog = true }
            )
            SettingsSecurity(
                isAuthenticationEnabled = settingsState.isAuthenticationEnabled,
                onAuthenticationClick = { showAuthenticationPrompt = true }
            )
        }
    }

    if (showThemeDialog) {
        SettingsThemeDialog(
            themeState = settingsState.themeState,
            onThemeChange = settingsActions::onThemeChange,
            onDismissRequest = { showThemeDialog = false }
        )
    }

    if (showFirstBackupDialog) {
        SettingsFirstActionConfirmationDialog(
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
        SettingsFirstActionConfirmationDialog(
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
            this.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        } else {
            this.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            this.data = "package:${context.packageName}".toUri()
        }
        this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}

internal data class SettingsState(
    val isNotificationEnabled: Boolean,
    val themeState: ThemeState,
    val isFirstBackup: Boolean,
    val isFirstRestore: Boolean,
    val isAuthenticationEnabled: Boolean,
    val result: DatabaseResultState?,
)

internal interface SettingsActions {
    fun onNavigateBack()
    fun onNotificationEnableChange(isEnabled: Boolean)
    fun onThemeChange(themeState: ThemeState)
    fun onNavigateToExport()
    fun onBackupData()
    fun onSetFirstBackupToFalse()
    fun onRestoreData(uri: Uri)
    fun onSetFirstRestoreToFalse()
    fun onRemoveAllData()
    fun onAuthenticationEnableChange(isEnabled: Boolean)
}