package com.android.monu.domain.usecase.save

import com.android.monu.domain.model.save.Save
import com.android.monu.domain.repository.SaveRepository
import kotlinx.coroutines.flow.Flow

class GetSaveByIdUseCase(
    private val repository: SaveRepository
) {
    operator fun invoke(saveId: Long): Flow<Save?> {
        return repository.getSaveById(saveId)
    }
}