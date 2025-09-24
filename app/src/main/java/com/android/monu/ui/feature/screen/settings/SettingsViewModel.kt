package com.android.monu.ui.feature.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.data.datastore.ThemeSetting
import com.android.monu.data.datastore.UserPreference
import com.android.monu.domain.usecase.finance.BackupDataUseCase
import com.android.monu.domain.usecase.finance.DeleteAllDataUseCase
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preference: UserPreference,
    private val backupDataUseCase: BackupDataUseCase,
    private val deleteAllDataUseCase: DeleteAllDataUseCase
) : ViewModel() {

    val themeSetting = preference.themeSetting
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeSetting.SYSTEM)

    private val _backupResult = MutableStateFlow<DatabaseResultMessage?>(null)
    val backupResult = _backupResult.asStateFlow()

    fun changeTheme(themeSetting: ThemeSetting) {
        viewModelScope.launch {
            preference.saveThemeSetting(themeSetting)
        }
    }

    fun backupData() {
        viewModelScope.launch {
            _backupResult.value = backupDataUseCase()
            delay(500)
            _backupResult.value = null
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            deleteAllDataUseCase()
        }
    }
}