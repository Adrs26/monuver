package com.android.monu.domain.usecase.save

import com.android.monu.domain.model.save.Save
import com.android.monu.domain.repository.SaveRepository
import com.android.monu.ui.feature.screen.saving.editsave.components.EditSaveContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage

class UpdateSaveUseCase(
    private val repository: SaveRepository
) {
    suspend operator fun invoke(saveState: EditSaveContentState): DatabaseResultMessage {
        when {
            saveState.title.isEmpty() -> return DatabaseResultMessage.EmptySaveTitle
            saveState.targetDate.isEmpty() -> return DatabaseResultMessage.EmptySaveTargetDate
            saveState.targetAmount == 0L -> return DatabaseResultMessage.EmptySaveTargetAmount
        }

        val save = Save(
            id = saveState.id,
            title = saveState.title,
            targetDate = saveState.targetDate,
            targetAmount = saveState.targetAmount,
            currentAmount = saveState.currentAmount,
            isActive = true
        )

        repository.updateSave(save)
        return DatabaseResultMessage.UpdateSaveSuccess
    }
}