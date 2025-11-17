package com.android.monuver.feature.saving.domain.usecase

import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

internal class GetAllInactiveSavingsUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(): Flow<List<SavingState>> {
        return repository.getAllInactiveSavings()
    }
}