package com.android.monu.ui.feature.screen.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.data.datastore.ThemeSetting
import com.android.monu.data.datastore.UserPreference
import com.android.monu.domain.usecase.finance.BackupDataUseCase
import com.android.monu.domain.usecase.finance.DeleteAllDataUseCase
import com.android.monu.domain.usecase.finance.ExportDataState
import com.android.monu.domain.usecase.finance.ExportDataToPdfUseCase
import com.android.monu.domain.usecase.finance.RestoreDataUseCase
import com.android.monu.ui.feature.screen.settings.export.components.ExportContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preference: UserPreference,
    private val exportDataToPdfUseCase: ExportDataToPdfUseCase,
    private val backupDataUseCase: BackupDataUseCase,
    private val restoreDataUseCase: RestoreDataUseCase,
    private val deleteAllDataUseCase: DeleteAllDataUseCase
) : ViewModel() {

    val themeSetting = preference.themeSetting
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeSetting.SYSTEM)

    val isFirstExport = preference.isFirstExport
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val isFirstBackup = preference.isFirstBackup
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val isFirstRestore = preference.isFirstRestore
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _exportProgress = MutableStateFlow<ExportDataState>(ExportDataState.Idle)
    val exportProgress = _exportProgress.asStateFlow()

    private val _processResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val processResult = _processResult.asStateFlow()

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

    fun exportDataToPdf(exportContentState: ExportContentState) {
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
        viewModelScope.launch {
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
}