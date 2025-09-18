package com.android.monu.domain.usecase.finance

import com.android.monu.domain.repository.FinanceRepository

class DeleteAllDataUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllData()
    }
}