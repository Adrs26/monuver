package com.android.monu.domain.usecase.finance

import android.util.Log
import com.android.monu.domain.manager.DataManager
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestoreDataUseCase(
    private val dataManager: DataManager,
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(stringUri: String): DatabaseResultMessage = withContext(Dispatchers.IO) {
        try {
            val backupData = dataManager.restoreData(stringUri)
            repository.restoreAllData(backupData)
            DatabaseResultMessage.RestoreDataSuccess
        } catch (e: Exception) {
            Log.e("RestoreDataUseCase", e.message.toString())
            DatabaseResultMessage.RestoreDataFailed
        }
    }
}