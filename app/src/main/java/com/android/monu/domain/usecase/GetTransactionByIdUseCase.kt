package com.android.monu.domain.usecase

import com.android.monu.domain.repository.TransactionRepository

class GetTransactionByIdUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(id: Long) = repository.getTransactionById(id)
}