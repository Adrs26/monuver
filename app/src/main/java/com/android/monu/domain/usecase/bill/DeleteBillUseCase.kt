package com.android.monu.domain.usecase.bill

import com.android.monu.domain.repository.BillRepository

class DeleteBillUseCase(
    private val repository: BillRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteBill(id)
    }
}