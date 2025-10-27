package com.android.monu.domain.usecase.finance

import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.utils.DateHelper
import com.android.monu.utils.TransactionChildCategory
import com.android.monu.utils.TransactionParentCategory
import com.android.monu.utils.TransactionType
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class CompleteSavingUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(savingId: Long, savingName: String, savingAmount: Long): DatabaseResultState {
        if (savingAmount == 0L) return DatabaseResultState.EmptySavingAmount

        val currentDate = LocalDate.now()
        val isoDate = currentDate.format(DateTimeFormatter.ISO_DATE)
        val (month, year) = DateHelper.getMonthAndYear(isoDate)

        val transactionState = TransactionState(
            title = "Penyelesaian Tabungan",
            type = TransactionType.EXPENSE,
            parentCategory = TransactionParentCategory.OTHER_EXPENSE,
            childCategory = TransactionChildCategory.SAVINGS_COMPLETE,
            date = isoDate,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = savingAmount,
            sourceId = savingId.toInt(),
            sourceName = savingName,
            isLocked = true,
            isSpecialCase = true
        )

        financeRepository.completeSaving(transactionState, savingId)
        return DatabaseResultState.CompleteSavingSuccess
    }
}