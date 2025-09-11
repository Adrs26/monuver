package com.android.monu.domain.usecase.save

import com.android.monu.domain.repository.SaveRepository
import kotlinx.coroutines.flow.Flow

class GetTotalSaveCurrentAmountUseCase(
    private val repository: SaveRepository
) {
    operator fun invoke(): Flow<Long?> {
        return repository.getTotalSaveCurrentAmount()
    }
}