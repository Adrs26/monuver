package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope

class GetAllTransactionsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ) = repository.getAllTransactions(query, type, month, year, scope)
}