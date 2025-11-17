package com.android.monuver.feature.home.domain.usecase

import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetRecentTransactionsUseCase(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<List<TransactionState>> {
        return repository.getRecentTransactions()
    }
}