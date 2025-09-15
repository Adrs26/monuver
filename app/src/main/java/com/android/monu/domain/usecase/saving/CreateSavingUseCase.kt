package com.android.monu.domain.usecase.saving

import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.repository.SavingRepository
import com.android.monu.ui.feature.screen.saving.addSaving.components.AddSavingContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class CreateSavingUseCase(
    private val repository: SavingRepository
) {
    suspend operator fun invoke(savingState: AddSavingContentState): DatabaseResultMessage {
        when {
            savingState.title.isEmpty() -> return DatabaseResultMessage.EmptySavingTitle
            savingState.targetDate.isEmpty() -> return DatabaseResultMessage.EmptySavingTargetDate
            savingState.targetAmount == 0L -> return DatabaseResultMessage.EmptySavingTargetAmount
        }

        val saving = Saving(
            title = savingState.title,
            targetDate = savingState.targetDate,
            targetAmount = savingState.targetAmount,
            currentAmount = 0,
            isActive = true
        )

        repository.createNewSaving(saving)
        return DatabaseResultMessage.CreateSavingSuccess
    }
}