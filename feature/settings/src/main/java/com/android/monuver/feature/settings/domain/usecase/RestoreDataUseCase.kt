package com.android.monuver.feature.settings.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.feature.settings.domain.manager.DataManager
import com.android.monuver.feature.settings.domain.repository.SettingsRepository

internal class RestoreDataUseCase(
    private val dataManager: DataManager,
    private val repository: SettingsRepository
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