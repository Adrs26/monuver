package com.android.monuver.core.domain.common

sealed class ThemeState {
    data object Light : ThemeState()
    data object Dark : ThemeState()
    data object System : ThemeState()
}