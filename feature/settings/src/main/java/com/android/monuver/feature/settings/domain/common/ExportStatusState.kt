package com.android.monuver.feature.settings.domain.common

import com.android.monuver.core.domain.common.DatabaseResultState

internal sealed class ExportStatusState {
    data object Idle: ExportStatusState()
    data object Progress : ExportStatusState()
    data object Success : ExportStatusState()
    data class Error(val error: DatabaseResultState) : ExportStatusState()
}