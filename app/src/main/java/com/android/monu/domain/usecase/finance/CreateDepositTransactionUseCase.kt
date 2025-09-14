package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.screen.saving.deposit.components.DepositContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.TransactionChildCategory
import com.android.monu.ui.feature.utils.TransactionParentCategory
import com.android.monu.ui.feature.utils.TransactionType

class CreateDepositTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(depositState: DepositContentState): DatabaseResultMessage {
        when {
            depositState.date.isEmpty() -> return DatabaseResultMessage.EmptyDepositDate
            depositState.amount == 0L -> return DatabaseResultMessage.EmptyDepositAmount
            depositState.accountId == 0 -> return DatabaseResultMessage.EmptyDepositAccount
        }

        val accountBalance = accountRepository.getAccountBalance(depositState.accountId)
        if (accountBalance == null || accountBalance < depositState.amount) {
            return DatabaseResultMessage.InsufficientAccountBalance
        }

        val (month, year) = DateHelper.getMonthAndYear(depositState.date)
        val transaction = Transaction(
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
            destinationId = depositState.saveId.toInt(),
            destinationName = depositState.saveName,
            saveId = depositState.saveId,
            isLocked = true
        )

        financeRepository.createDepositTransaction(depositState.saveId, transaction)
        return DatabaseResultMessage.CreateDepositTransactionSuccess
    }
}