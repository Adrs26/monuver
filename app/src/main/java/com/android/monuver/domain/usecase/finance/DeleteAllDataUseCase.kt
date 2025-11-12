package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.repository.FinanceRepository

class DeleteAllDataUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllData()
    }
}