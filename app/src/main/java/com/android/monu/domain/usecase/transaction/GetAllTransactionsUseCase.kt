package com.android.monu.domain.usecase.transaction

import androidx.paging.PagingData
import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.repository.TransactionRepository
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
        return repository.getAllTransactions(query, type, month, year, scope)
    }
}