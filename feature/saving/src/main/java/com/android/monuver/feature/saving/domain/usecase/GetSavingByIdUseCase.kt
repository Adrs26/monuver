package com.android.monuver.feature.saving.domain.usecase

import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

internal class GetSavingByIdUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(saveId: Long): Flow<SavingState?> {
        return repository.getSavingById(saveId)
    }
}