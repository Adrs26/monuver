package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.DepositWithdrawState
import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.FinanceRepository
import com.android.monuver.domain.repository.SavingRepository
import com.android.monuver.utils.DateHelper
import com.android.monuver.utils.TransactionChildCategory
import com.android.monuver.utils.TransactionParentCategory
import com.android.monuver.utils.TransactionType

class CreateWithdrawTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val savingRepository: SavingRepository
) {
    suspend operator fun invoke(withdrawState: DepositWithdrawState): DatabaseResultState {
        when {
            withdrawState.date.isEmpty() -> return DatabaseResultState.EmptyWithdrawDate
            withdrawState.amount == 0L -> return DatabaseResultState.EmptyWithdrawAmount
            withdrawState.accountId == 0 -> return DatabaseResultState.EmptyWithdrawAccount
        }

        val savingBalance = savingRepository.getSavingBalance(withdrawState.savingId)
        if (savingBalance == null || savingBalance < withdrawState.amount) {
            return DatabaseResultState.InsufficientSavingBalance
        }

        val (month, year) = DateHelper.getMonthAndYear(withdrawState.date)
        val transactionState = TransactionState(
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
            isLocked = true,
            isSpecialCase = true
        )

        financeRepository.createWithdrawTransaction(withdrawState.savingId, transactionState)
        return DatabaseResultState.CreateWithdrawTransactionSuccess
    }
}