package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.DepositWithdrawState
import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.AccountRepository
import com.android.monuver.domain.repository.FinanceRepository
import com.android.monuver.utils.DateHelper
import com.android.monuver.utils.TransactionChildCategory
import com.android.monuver.utils.TransactionParentCategory
import com.android.monuver.utils.TransactionType

class CreateDepositTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(depositState: DepositWithdrawState): DatabaseResultState {
        when {
            depositState.date.isEmpty() -> return DatabaseResultState.EmptyDepositDate
            depositState.amount == 0L -> return DatabaseResultState.EmptyDepositAmount
            depositState.accountId == 0 -> return DatabaseResultState.EmptyDepositAccount
        }

        val accountBalance = accountRepository.getAccountBalance(depositState.accountId)
        if (accountBalance == null || accountBalance < depositState.amount) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        val (month, year) = DateHelper.getMonthAndYear(depositState.date)
        val transactionState = TransactionState(
            title = "Setoran Tabungan",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.SAVINGS_IN,
            date = depositState.date,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = depositState.amount,
            sourceId = depositState.accountId,
            sourceName = depositState.accountName,
            destinationId = depositState.savingId.toInt(),
            destinationName = depositState.savingName,
            saveId = depositState.savingId,
            isLocked = true,
            isSpecialCase = true
        )

        financeRepository.createDepositTransaction(depositState.savingId, transactionState)
        return DatabaseResultState.CreateDepositTransactionSuccess
    }
}