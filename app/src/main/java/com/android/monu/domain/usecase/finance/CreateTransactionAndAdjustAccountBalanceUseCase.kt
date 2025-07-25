package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.utils.DateHelper

class CreateTransactionAndAdjustAccountBalanceUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        return try {
            val (month, year) = DateHelper.getMonthAndYear(transaction.date)
            val transaction = Transaction(
                id = 0,
                title = transaction.title,
                type = transaction.type,
                parentCategory = transaction.parentCategory,
                childCategory = transaction.childCategory,
                date = transaction.date,
                month = month,
                year = year,
                timeStamp = System.currentTimeMillis(),
                amount = transaction.amount,
                sourceId = transaction.sourceId,
                sourceName = transaction.sourceName,
                destinationId = transaction.destinationId,
                destinationName = transaction.destinationName
            )

            val accountBalance = accountRepository.getAccountBalance(transaction.sourceId)

            if (accountBalance == null || accountBalance < transaction.amount) {
                Result.failure(IllegalArgumentException("Saldo akun tidak mencukupi"))
            } else {
                val transactionId = financeRepository
                    .createTransactionAndAdjustAccountBalance(transaction)
                Result.success(transactionId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}