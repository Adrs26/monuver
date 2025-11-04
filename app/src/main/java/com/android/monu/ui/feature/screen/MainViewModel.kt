package com.android.monu.ui.feature.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.data.datastore.ThemeSetting
import com.android.monu.data.datastore.UserPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    preference: UserPreference
) : ViewModel() {

    val themeSetting = preference.themeSetting
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeSetting.SYSTEM)

    val isAuthenticationEnabled = preference.isAuthenticationEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    fun setAuthenticationStatus(authenticated: Boolean) {
        _isAuthenticated.value = authenticated
    }
}