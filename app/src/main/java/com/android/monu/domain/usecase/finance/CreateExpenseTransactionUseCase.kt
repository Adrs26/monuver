package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.screen.transaction.addtransaction.components.AddTransactionContentState
import com.android.monu.presentation.utils.DateHelper

class CreateExpenseTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transactionState: AddTransactionContentState): Result<Long> {
        return try {
            val (month, year) = DateHelper.getMonthAndYear(transactionState.date)
            val transaction = Transaction(
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

            if (accountBalance == null || accountBalance < transaction.amount) {
                Result.failure(IllegalArgumentException("Saldo akun tidak mencukupi"))
            } else {
                val transactionId = financeRepository.createExpenseTransaction(transaction)
                Result.success(transactionId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}