package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.TransactionChildCategory
import com.android.monu.ui.feature.utils.TransactionParentCategory
import com.android.monu.ui.feature.utils.TransactionType
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class CompleteSavingUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(savingId: Long, savingName: String, savingAmount: Long): DatabaseResultMessage {
        val currentDate = LocalDate.now()
        val isoDate = currentDate.format(DateTimeFormatter.ISO_DATE)
        val (month, year) = DateHelper.getMonthAndYear(isoDate)

        val transaction = Transaction(
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

        financeRepository.completeSaving(transaction, savingId)
        return DatabaseResultMessage.CompleteSavingSuccess
    }
}