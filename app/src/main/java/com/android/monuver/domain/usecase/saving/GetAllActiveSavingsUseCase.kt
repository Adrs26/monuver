package com.android.monuver.domain.usecase.saving

import com.android.monuver.domain.model.SavingState
import com.android.monuver.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

class GetAllActiveSavingsUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(): Flow<List<SavingState>> {
        return repository.getAllActiveSavings()
    }
}