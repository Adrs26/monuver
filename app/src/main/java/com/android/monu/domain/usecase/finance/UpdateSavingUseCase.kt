package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.screen.saving.editSaving.components.EditSavingContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class UpdateSavingUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(savingState: EditSavingContentState): DatabaseResultMessage {
        when {
            savingState.title.isEmpty() -> return DatabaseResultMessage.EmptySaveTitle
            savingState.targetDate.isEmpty() -> return DatabaseResultMessage.EmptySaveTargetDate
            savingState.targetAmount == 0L -> return DatabaseResultMessage.EmptySaveTargetAmount
        }

        val saving = Saving(
            id = savingState.id,
            title = savingState.title,
            targetDate = savingState.targetDate,
            targetAmount = savingState.targetAmount,
            currentAmount = savingState.currentAmount,
            isActive = true
        )

        repository.updateSaving(saving)
        return DatabaseResultMessage.UpdateSaveSuccess
    }
}