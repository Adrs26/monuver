package com.android.monuver.domain.usecase.saving

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AddSavingState
import com.android.monuver.domain.model.SavingState
import com.android.monuver.domain.repository.SavingRepository

class CreateSavingUseCase(
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