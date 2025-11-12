package com.android.monuver.domain.usecase.bill

import com.android.monuver.domain.repository.BillRepository

class DeleteBillUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteBillById(id)
    }
}