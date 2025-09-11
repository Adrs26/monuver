package com.android.monu.domain.usecase.save

import com.android.monu.domain.model.save.Save
import com.android.monu.domain.repository.SaveRepository
import com.android.monu.ui.feature.screen.saving.addsave.components.AddSaveContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class CreateSaveUseCase(
    private val repository: SaveRepository
) {
    suspend operator fun invoke(saveState: AddSaveContentState): DatabaseResultMessage {
        when {
            saveState.title.isEmpty() -> return DatabaseResultMessage.EmptySaveTitle
            saveState.targetDate.isEmpty() -> return DatabaseResultMessage.EmptySaveTargetDate
            saveState.targetAmount == 0L -> return DatabaseResultMessage.EmptySaveTargetAmount
        }

        val save = Save(
            title = saveState.title,
            targetDate = saveState.targetDate,
            targetAmount = saveState.targetAmount,
            currentAmount = 0,
            isActive = true
        )

        repository.createNewSave(save)
        return DatabaseResultMessage.CreateSaveSuccess
    }
}