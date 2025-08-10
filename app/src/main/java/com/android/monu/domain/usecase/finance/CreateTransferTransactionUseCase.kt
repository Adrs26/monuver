package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.screen.transaction.transfer.components.TransferContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.TransactionChildCategory
import com.android.monu.presentation.utils.TransactionParentCategory
import com.android.monu.presentation.utils.TransactionType

class CreateTransferTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transferState: TransferContentState): DatabaseResultMessage {
        when {
            transferState.sourceId == 0 -> return DatabaseResultMessage.EmptyTransactionSource
            transferState.destinationId == 0 -> return DatabaseResultMessage.EmptyTransactionDestination
            transferState.date.isEmpty() -> return DatabaseResultMessage.EmptyTransactionDate
            transferState.amount == 0L -> return DatabaseResultMessage.EmptyTransactionAmount
        }

        val (month, year) = DateHelper.getMonthAndYear(transferState.date)
        val transaction = Transaction(
            title = "Transfer ke akun ${transferState.destinationName}",
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
            destinationName = transferState.destinationName
        )

        val accountBalance = accountRepository.getAccountBalance(transaction.sourceId)

        if (accountBalance == null || accountBalance < transaction.amount) {
            return DatabaseResultMessage.InsufficientAccountBalance
        }

        financeRepository.createTransferTransaction(transaction)
        return DatabaseResultMessage.CreateTransactionSuccess
    }
}