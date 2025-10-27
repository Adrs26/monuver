package com.android.monu.domain.usecase.finance

import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.model.TransferState
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.utils.DateHelper
import com.android.monu.utils.TransactionChildCategory
import com.android.monu.utils.TransactionParentCategory
import com.android.monu.utils.TransactionType

class CreateTransferTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transferState: TransferState): DatabaseResultState {
        when {
            transferState.sourceId == 0 -> return DatabaseResultState.EmptyTransactionSource
            transferState.destinationId == 0 -> return DatabaseResultState.EmptyTransactionDestination
            transferState.date.isEmpty() -> return DatabaseResultState.EmptyTransactionDate
            transferState.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
        }

        val (month, year) = DateHelper.getMonthAndYear(transferState.date)
        val transactionState = TransactionState(
            title = "Transfer Saldo",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.TRANSFER_ACCOUNT,
            date = transferState.date,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = transferState.amount,
            sourceId = transferState.sourceId,
            sourceName = transferState.sourceName,
            destinationId = transferState.destinationId,
            destinationName = transferState.destinationName,
            isLocked = true,
            isSpecialCase = true
        )

        val accountBalance = accountRepository.getAccountBalance(transactionState.sourceId)

        if (accountBalance == null || accountBalance < transactionState.amount) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        financeRepository.createTransferTransaction(transactionState)
        return DatabaseResultState.CreateTransactionSuccess
    }
}