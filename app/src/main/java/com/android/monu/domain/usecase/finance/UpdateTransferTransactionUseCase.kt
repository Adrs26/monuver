package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.screen.transaction.edittransfer.components.EditTransferContentState
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.TransactionChildCategory
import com.android.monu.presentation.utils.TransactionParentCategory
import com.android.monu.presentation.utils.TransactionType

class UpdateTransferTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transferState: EditTransferContentState): Result<Int> {
        return try {
            val (month, year) = DateHelper.getMonthAndYear(transferState.date)
            val transaction = Transaction(
                id = transferState.id,
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
            val difference = transaction.amount - transferState.startAmount

            if (accountBalance == null || accountBalance < difference) {
                Result.failure(IllegalArgumentException("Saldo akun tidak mencukupi"))
            } else {
                val rowUpdated = financeRepository.updateTransferTransaction(
                    transaction,
                    transferState.startAmount
                )
                Result.success(rowUpdated)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}