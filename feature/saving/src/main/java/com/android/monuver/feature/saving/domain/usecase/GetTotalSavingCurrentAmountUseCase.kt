package com.android.monuver.feature.saving.domain.usecase

import com.android.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

internal class GetTotalSavingCurrentAmountUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(): Flow<Long?> {
        return repository.getTotalSavingCurrentAmount()
    }
}