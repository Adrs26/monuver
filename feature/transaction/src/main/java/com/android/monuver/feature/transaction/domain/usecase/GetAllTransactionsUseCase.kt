package com.android.monuver.feature.transaction.domain.usecase

import androidx.paging.PagingData
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class GetAllTransactionsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<TransactionState>> {
        return repository.getAllTransactions(
            query = query,
            type = type,
            month = month,
            year = year,
            scope = scope
        )
    }
}