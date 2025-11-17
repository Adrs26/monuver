package com.android.monuver.feature.saving.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.feature.saving.domain.model.EditSavingState
import com.android.monuver.feature.saving.domain.repository.SavingRepository

internal class UpdateSavingUseCase(
    private val repository: SavingRepository
) {
    suspend operator fun invoke(savingState: EditSavingState): DatabaseResultState {
        when {
            savingState.title.isEmpty() -> return DatabaseResultState.EmptySavingTitle
            savingState.targetDate.isEmpty() -> return DatabaseResultState.EmptySavingTargetDate
            savingState.targetAmount == 0L -> return DatabaseResultState.EmptySavingTargetAmount
        }

        val saving = SavingState(
            id = savingState.id,
            title = savingState.title,
            targetDate = savingState.targetDate,
            targetAmount = savingState.targetAmount,
            currentAmount = savingState.currentAmount,
            isActive = true
        )

        repository.updateSaving(saving)
        return DatabaseResultState.UpdateSavingSuccess
    }
}