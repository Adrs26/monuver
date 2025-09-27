package com.android.monu.domain.usecase.finance

import android.net.Uri
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestoreDataUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(uri: Uri): DatabaseResultMessage = withContext(Dispatchers.IO) {
        try {
            repository.restoreAllData(uri)
            DatabaseResultMessage.RestoreDataSuccess
        } catch (_: Exception) {
            DatabaseResultMessage.RestoreDataFailed
        }
    }
}