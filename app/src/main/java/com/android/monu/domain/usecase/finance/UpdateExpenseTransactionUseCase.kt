package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.screen.transaction.edittransaction.components.EditTransactionContentState
import com.android.monu.presentation.utils.DateHelper

class UpdateExpenseTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transactionState: EditTransactionContentState): Result<Int> {
        return try {
            val (month, year) = DateHelper.getMonthAndYear(transactionState.date)
            val transaction = Transaction(
                id = transactionState.id,
                title = transactionState.title,
                type = transactionState.type,
                parentCategory = transactionState.parentCategory,
                childCategory = transactionState.childCategory,
                date = transactionState.date,
                month = month,
                year = year,
                timeStamp = System.currentTimeMillis(),
                amount = transactionState.amount,
                sourceId = transactionState.sourceId,
                sourceName = transactionState.sourceName,
            )

            val accountBalance = accountRepository.getAccountBalance(transaction.sourceId)
            val difference = transaction.amount - transactionState.startAmount

            if (accountBalance == null || accountBalance < difference) {
                Result.failure(IllegalArgumentException("Saldo akun tidak mencukupi"))
            } else {
                val rowUpdated = financeRepository.updateExpenseTransaction(
                    transaction,
                    transactionState.startAmount
                )
                Result.success(rowUpdated)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}