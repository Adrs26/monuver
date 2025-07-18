package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.repository.TransactionRepository

class GetTotalTransactionAmountUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(type: Int) = repository.getTotalTransactionAmount(type)
}