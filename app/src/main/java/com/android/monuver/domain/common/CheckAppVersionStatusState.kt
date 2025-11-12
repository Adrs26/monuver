package com.android.monuver.domain.common

sealed class CheckAppVersionStatusState {
    data object Progress : CheckAppVersionStatusState()
    data object Success : CheckAppVersionStatusState()
    data object Error : CheckAppVersionStatusState()
}