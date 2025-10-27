package com.android.monu.domain.common

sealed class ExportStatusState {
    data object Idle: ExportStatusState()
    data object Progress : ExportStatusState()
    data object Success : ExportStatusState()
    data class Error(val error: DatabaseResultState) : ExportStatusState()
}