package com.android.monu.domain.usecase.saving

import com.android.monu.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

class GetTotalSavingCurrentAmountUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(): Flow<Long?> {
        return repository.getTotalSavingCurrentAmount()
    }
}