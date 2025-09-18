package com.android.monu.ui.feature.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.data.datastore.ThemeSetting
import com.android.monu.data.datastore.UserPreference
import com.android.monu.domain.usecase.finance.DeleteAllDataUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preference: UserPreference,
    private val deleteAllDataUseCase: DeleteAllDataUseCase
) : ViewModel() {

    val themeSetting = preference.themeSetting
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeSetting.SYSTEM)

    fun changeTheme(themeSetting: ThemeSetting) {
        viewModelScope.launch {
            preference.saveThemeSetting(themeSetting)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            deleteAllDataUseCase()
        }
    }
}