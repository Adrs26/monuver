package com.android.monuver.ui.feature.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.data.datastore.ThemeSetting
import com.android.monuver.data.datastore.UserPreferences
import com.android.monuver.domain.common.CheckAppVersionStatusState
import com.android.monuver.domain.usecase.finance.CheckAppVersionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    val preference: UserPreferences,
    val checkAppVersionUseCase: CheckAppVersionUseCase
) : ViewModel() {

    val isFirstTime = preference.isFirstTime
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _checkAppVersionStatus = MutableStateFlow<CheckAppVersionStatusState>(
        CheckAppVersionStatusState.Progress
    )
    val checkAppVersionStatus = _checkAppVersionStatus.asStateFlow()

    val themeSetting = preference.themeSetting
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeSetting.SYSTEM)

    val isAuthenticationEnabled = preference.isAuthenticationEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    fun setIsFirstTimeToFalse() {
        viewModelScope.launch {
            preference.setFirstTimeToFalse()
        }
    }

    fun checkAppVersion() {
        viewModelScope.launch {
            checkAppVersionUseCase().collect { status ->
                _checkAppVersionStatus.value = status
            }
        }
    }

    fun setAuthenticationStatus(authenticated: Boolean) {
        _isAuthenticated.value = authenticated
    }
}