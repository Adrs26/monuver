package com.android.monu.domain.usecase.finance

import android.util.Log
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.manager.DataManager
import com.android.monu.domain.repository.FinanceRepository

class RestoreDataUseCase(
    private val dataManager: DataManager,
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(stringUri: String): DatabaseResultState {
        return try {
            val backupData = dataManager.restoreData(stringUri)
            repository.restoreAllData(backupData)
            DatabaseResultState.RestoreDataSuccess
        } catch (e: Exception) {
            Log.e("RestoreDataUseCase", e.message.toString())
            DatabaseResultState.RestoreDataFailed
        }
    }
}