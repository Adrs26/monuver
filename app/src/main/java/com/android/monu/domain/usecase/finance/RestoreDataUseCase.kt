package com.android.monu.domain.usecase.finance

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
        } catch (_: Exception) {
            DatabaseResultState.RestoreDataFailed
        }
    }
}