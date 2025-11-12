package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.EditSavingState
import com.android.monuver.domain.model.SavingState
import com.android.monuver.domain.repository.FinanceRepository

class UpdateSavingUseCase(
    private val repository: FinanceRepository
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