package com.android.monu.ui.feature.screen.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.data.datastore.ThemeSetting
import com.android.monu.data.datastore.UserPreferences
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.common.ExportStatusState
import com.android.monu.domain.model.ExportState
import com.android.monu.domain.usecase.finance.BackupDataUseCase
import com.android.monu.domain.usecase.finance.DeleteAllDataUseCase
import com.android.monu.domain.usecase.finance.ExportDataToPdfUseCase
import com.android.monu.domain.usecase.finance.RestoreDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preference: UserPreferences,
    private val exportDataToPdfUseCase: ExportDataToPdfUseCase,
    private val backupDataUseCase: BackupDataUseCase,
    private val restoreDataUseCase: RestoreDataUseCase,
    private val deleteAllDataUseCase: DeleteAllDataUseCase
) : ViewModel() {

    val isNotificationEnabled = preference.isNotificationEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val themeSetting = preference.themeSetting
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeSetting.SYSTEM)

    val isFirstExport = preference.isFirstExport
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val isFirstBackup = preference.isFirstBackup
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val isFirstRestore = preference.isFirstRestore
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val isAuthenticationEnabled = preference.isAuthenticationEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _exportProgress = MutableStateFlow<ExportStatusState>(ExportStatusState.Idle)
    val exportProgress = _exportProgress.asStateFlow()

    private val _processResult = MutableStateFlow<DatabaseResultState?>(null)
    val processResult = _processResult.asStateFlow()

    fun setNotification(isEnabled: Boolean) {
        viewModelScope.launch {
            preference.setNotification(isEnabled)
        }
    }

    fun changeTheme(themeSetting: ThemeSetting) {
        viewModelScope.launch {
            preference.saveThemeSetting(themeSetting)
        }
    }

    fun setIsFirstExportToFalse() {
        viewModelScope.launch {
            preference.setIsFirstExportToFalse()
        }
    }

    fun exportDataToPdf(exportContentState: ExportState) {
        viewModelScope.launch(Dispatchers.IO) {
            exportDataToPdfUseCase(exportContentState).collect { progress ->
                _exportProgress.value = progress
            }
        }
    }

    fun setIsFirstBackupToFalse() {
        viewModelScope.launch {
            preference.setIsFirstBackupToFalse()
        }
    }

    fun backupData() {
        viewModelScope.launch(Dispatchers.IO) {
            _processResult.value = backupDataUseCase()
            delay(500)
            _processResult.value = null
        }
    }

    fun setIsFirstRestoreToFalse() {
        viewModelScope.launch {
            preference.setIsFirstRestoreToFalse()
        }
    }

    fun restoreData(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
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
            preference.setAuthentication(isEnabled)
        }
    }
}