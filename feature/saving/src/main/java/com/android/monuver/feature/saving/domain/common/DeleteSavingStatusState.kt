package com.android.monuver.feature.saving.domain.common

internal sealed class DeleteSavingStatusState {
    data object Idle : DeleteSavingStatusState()
    data class Progress(val current: Int, val total: Int) : DeleteSavingStatusState()
    data object Success : DeleteSavingStatusState()
    data class Error(val throwable: Throwable) : DeleteSavingStatusState()
}