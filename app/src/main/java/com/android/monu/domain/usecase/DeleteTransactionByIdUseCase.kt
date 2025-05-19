package com.android.monu.domain.usecase

import com.android.monu.domain.repository.TransactionRepository

class DeleteTransactionByIdUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteTransactionById(id)
}