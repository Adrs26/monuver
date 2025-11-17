package com.android.monuver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monuver.core.data.datastore.UserPreferences
import com.android.monuver.core.domain.common.CheckAppVersionStatusState
import com.android.monuver.core.domain.common.ThemeState
import com.android.monuver.core.domain.usecase.CheckAppVersionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class MainViewModel(
    val preferences: UserPreferences,
    val checkAppVersionUseCase: CheckAppVersionUseCase
) : ViewModel() {

    val isFirstTime = preferences.isFirstTime
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    private val _checkAppVersionStatus = MutableStateFlow<CheckAppVersionStatusState>(
        CheckAppVersionStatusState.Progress
    )
    val checkAppVersionStatus = _checkAppVersionStatus.asStateFlow()

    val themeState = preferences.themeState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeState.System
        )

    val isAuthenticationEnabled = preferences.isAuthenticationEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    fun setIsFirstTimeToFalse() {
        viewModelScope.launch {
            preferences.setFirstTimeToFalse()
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