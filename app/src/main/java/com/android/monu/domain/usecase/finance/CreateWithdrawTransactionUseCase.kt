package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.domain.repository.SavingRepository
import com.android.monu.ui.feature.screen.saving.withdraw.components.WithdrawContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.TransactionChildCategory
import com.android.monu.ui.feature.utils.TransactionParentCategory
import com.android.monu.ui.feature.utils.TransactionType

class CreateWithdrawTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val savingRepository: SavingRepository
) {
    suspend operator fun invoke(withdrawState: WithdrawContentState): DatabaseResultMessage {
        when {
            withdrawState.date.isEmpty() -> return DatabaseResultMessage.EmptyWithdrawDate
            withdrawState.amount == 0L -> return DatabaseResultMessage.EmptyWithdrawAmount
            withdrawState.accountId == 0 -> return DatabaseResultMessage.EmptyWithdrawAccount
        }

        val savingBalance = savingRepository.getSavingBalance(withdrawState.savingId)
        if (savingBalance == null || savingBalance < withdrawState.amount) {
            return DatabaseResultMessage.InsufficientAccountBalance
        }

        val (month, year) = DateHelper.getMonthAndYear(withdrawState.date)
        val transaction = Transaction(
            title = "Penarikan Tabungan",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.SAVINGS_OUT,
            date = withdrawState.date,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = withdrawState.amount,
            sourceId = withdrawState.savingId.toInt(),
            sourceName = withdrawState.savingName,
            destinationId = withdrawState.accountId,
            destinationName = withdrawState.accountName,
            saveId = withdrawState.savingId,
            isLocked = true
        )

        financeRepository.createWithdrawTransaction(withdrawState.savingId, transaction)
        return DatabaseResultMessage.CreateWithdrawTransactionSuccess
    }
}