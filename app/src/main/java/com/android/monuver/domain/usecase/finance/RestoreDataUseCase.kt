package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.manager.DataManager
import com.android.monuver.domain.repository.FinanceRepository

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