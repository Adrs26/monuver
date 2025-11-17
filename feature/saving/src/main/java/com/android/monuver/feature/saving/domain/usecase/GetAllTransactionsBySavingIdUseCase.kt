package com.android.monuver.feature.saving.domain.usecase

import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow

internal class GetAllTransactionsBySavingIdUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(savingId: Long): Flow<List<TransactionState>> {
        return repository.getTransactionsBySavingId(savingId)
    }
}