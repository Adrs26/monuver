package com.android.monu.domain.usecase.saving

import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

class GetSavingByIdUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(saveId: Long): Flow<Saving?> {
        return repository.getSavingById(saveId)
    }
}