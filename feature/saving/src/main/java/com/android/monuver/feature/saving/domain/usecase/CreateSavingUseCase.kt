package com.android.monuver.feature.saving.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.feature.saving.domain.model.AddSavingState
import com.android.monuver.feature.saving.domain.repository.SavingRepository

internal class CreateSavingUseCase(
    private val repository: SavingRepository
) {
    suspend operator fun invoke(savingState: AddSavingState): DatabaseResultState {
        when {
            savingState.title.isEmpty() -> return DatabaseResultState.EmptySavingTitle
            savingState.targetDate.isEmpty() -> return DatabaseResultState.EmptySavingTargetDate
            savingState.targetAmount == 0L -> return DatabaseResultState.EmptySavingTargetAmount
        }

        val saving = SavingState(
            title = savingState.title,
            targetDate = savingState.targetDate,
            targetAmount = savingState.targetAmount,
            currentAmount = 0,
            isActive = true
        )

        repository.createNewSaving(saving)
        return DatabaseResultState.CreateSavingSuccess
    }
}