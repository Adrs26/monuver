package com.android.monu.domain.usecase.saving

import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

class GetAllActiveSavingsUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(): Flow<List<Saving>> {
        return repository.getAllActiveSavings()
    }
}