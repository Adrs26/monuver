package com.android.monu.domain.common

sealed class DeleteSavingStatusState {
    data object Idle : DeleteSavingStatusState()
    data class Progress(val current: Int, val total: Int) : DeleteSavingStatusState()
    data object Success : DeleteSavingStatusState()
    data class Error(val throwable: Throwable) : DeleteSavingStatusState()
}