package com.android.monu.domain.usecase

import com.android.monu.domain.repository.TransactionRepository

class GetRecentTransactionUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke() = repository.getRecentTransactions()
}