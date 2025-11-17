package com.android.monuver.feature.settings.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.core.data.datastore.UserPreferences
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.common.ThemeState
import com.android.monuver.feature.settings.domain.common.ExportStatusState
import com.android.monuver.feature.settings.domain.model.ExportState
import com.android.monuver.feature.settings.domain.usecase.BackupDataUseCase
import com.android.monuver.feature.settings.domain.usecase.DeleteAllDataUseCase
import com.android.monuver.feature.settings.domain.usecase.ExportDataToPdfUseCase
import com.android.monuver.feature.settings.domain.usecase.RestoreDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val preferences: UserPreferences,
    private val exportDataToPdfUseCase: ExportDataToPdfUseCase,
    private val backupDataUseCase: BackupDataUseCase,
    private val restoreDataUseCase: RestoreDataUseCase,
    private val deleteAllDataUseCase: DeleteAllDataUseCase
) : ViewModel() {

    val isNotificationEnabled = preferences.isNotificationEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val themeState = preferences.themeState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeState.System
        )

    val isFirstExport = preferences.isFirstExport
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val isFirstBackup = preferences.isFirstBackup
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val isFirstRestore = preferences.isFirstRestore
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val isAuthenticationEnabled = preferences.isAuthenticationEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _exportStatus = MutableStateFlow<ExportStatusState>(ExportStatusState.Idle)
    val exportStatus = _exportStatus.asStateFlow()

    private val _processResult = MutableStateFlow<DatabaseResultState?>(null)
    val processResult = _processResult.asStateFlow()

    fun setNotification(isEnabled: Boolean) {
        viewModelScope.launch {
            preferences.setNotification(isEnabled)
        }
    }

    fun changeTheme(themeState: ThemeState) {
        viewModelScope.launch {
            preferences.setThemeState(themeState)
        }
    }

    fun setIsFirstExportToFalse() {
        viewModelScope.launch {
            preferences.setIsFirstExportToFalse()
        }
    }

    fun exportDataToPdf(exportState: ExportState) {
        viewModelScope.launch {
            exportDataToPdfUseCase(exportState).collect { status ->
                _exportStatus.value = status
            }
        }
    }

    fun setIsFirstBackupToFalse() {
        viewModelScope.launch {
            preferences.setIsFirstBackupToFalse()
        }
    }

    fun backupData() {
        viewModelScope.launch {
            _processResult.value = backupDataUseCase()
            delay(500)
            _processResult.value = null
        }
    }

    fun setIsFirstRestoreToFalse() {
        viewModelScope.launch {
            preferences.setIsFirstRestoreToFalse()
        }
    }

    fun restoreData(uri: Uri) {
        viewModelScope.launch {
            _processResult.value = restoreDataUseCase(uri.toString())
            delay(500)
            _processResult.value = null
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            deleteAllDataUseCase()
        }
    }

    fun setAuthentication(isEnabled: Boolean) {
        viewModelScope.launch {
            preferences.setAuthentication(isEnabled)
        }
    }
}