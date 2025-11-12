package com.android.monuver.domain.usecase.saving

import com.android.monuver.domain.model.SavingState
import com.android.monuver.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

class GetSavingByIdUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(saveId: Long): Flow<SavingState?> {
        return repository.getSavingById(saveId)
    }
}