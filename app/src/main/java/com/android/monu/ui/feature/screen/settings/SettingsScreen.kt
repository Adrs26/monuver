package com.android.monu.ui.feature.screen.settings

import android.Manifest
import android.net.Uri
import android.os.Build
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
import com.android.monu.R
import com.android.monu.data.datastore.ThemeSetting
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.components.ConfirmationDialog
import com.android.monu.ui.feature.screen.settings.components.FirstBackupRestoreConfirmation
import com.android.monu.ui.feature.screen.settings.components.SettingsApplicationData
import com.android.monu.ui.feature.screen.settings.components.SettingsPreference
import com.android.monu.ui.feature.screen.settings.components.SettingsSecurity
import com.android.monu.ui.feature.screen.settings.components.SettingsThemeDialog
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    themeSetting: ThemeSetting,
    isFirstBackup: Boolean,
    isFirstRestore: Boolean,
    processResult: DatabaseResultMessage?,
    onNavigateBack: () -> Unit,
    onThemeChange: (ThemeSetting) -> Unit,
    onBackupData: () -> Unit,
    onSetFirstBackupToFalse: () -> Unit,
    onRestoreData: (Uri) -> Unit,
    onSetFirstRestoreToFalse: () -> Unit,
    onRemoveAllData: () -> Unit
) {
    var checked by remember { mutableStateOf(true) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showFirstBackupDialog by remember { mutableStateOf(false) }
    var showFirstRestoreDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val storagePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val pickJsonFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { onRestoreData(it) }
    }

    LaunchedEffect(processResult) {
        processResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.settings),
                onNavigateBack = onNavigateBack
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
                isNotificationEnabled = checked,
                themeSetting = themeSetting,
                onNotificationClicked = { },
                onThemeClicked = { showThemeDialog = true },
                modifier = Modifier.padding(top = 8.dp)
            )
            SettingsApplicationData(
                onExportDataClicked = {  },
                onBackupDataClicked = {
                    if (isFirstBackup) {
                        showFirstBackupDialog = true
                    } else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                            !storagePermissionState.status.isGranted) {
                            storagePermissionState.launchPermissionRequest()
                        } else {
                            onBackupData()
                        }
                    }
                },
                onRestoreDataClicked = {
                    if (isFirstRestore) {
                        showFirstRestoreDialog = true
                    } else {
                        pickJsonFileLauncher.launch(arrayOf("application/json"))
                    }
                },
                onDeleteDataClicked = { showDeleteDialog = true }
            )
            SettingsSecurity(
                onPinAuthenticationClicked = {  },
                onFingerprintAuthenticationClicked = {  }
            )
        }
    }

    if (showThemeDialog) {
        SettingsThemeDialog(
            themeSetting = themeSetting,
            onThemeChange = onThemeChange,
            onDismissRequest = { showThemeDialog = false }
        )
    }

    if (showFirstBackupDialog) {
        FirstBackupRestoreConfirmation(
            text = stringResource(R.string.first_backup_confirmation),
            onDismissRequest = { showFirstBackupDialog = false },
            onConfirmRequest = {
                showFirstBackupDialog = false
                onSetFirstBackupToFalse()
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                    !storagePermissionState.status.isGranted) {
                    storagePermissionState.launchPermissionRequest()
                } else {
                    onBackupData()
                }
            }
        )
    }

    if (showFirstRestoreDialog) {
        FirstBackupRestoreConfirmation(
            text = stringResource(R.string.first_restore_confirmation),
            onDismissRequest = { showFirstRestoreDialog = false },
            onConfirmRequest = {
                showFirstRestoreDialog = false
                onSetFirstRestoreToFalse()
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
                onRemoveAllData()
                context.getString(
                    DatabaseResultMessage.DeleteAllDataSuccess.message
                ).showMessageWithToast(context)
            }
        )
    }
}