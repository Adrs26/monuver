package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AddTransactionState
import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.FinanceRepository
import com.android.monuver.utils.DateHelper

class CreateIncomeTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(transactionState: AddTransactionState): DatabaseResultState {
        when {
            transactionState.title.isEmpty() -> return DatabaseResultState.EmptyTransactionTitle
            transactionState.childCategory == 0 -> return DatabaseResultState.EmptyTransactionCategory
            transactionState.date.isEmpty() -> return DatabaseResultState.EmptyTransactionDate
            transactionState.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
            transactionState.sourceId == 0 -> return DatabaseResultState.EmptyTransactionSource
        }

        val (month, year) = DateHelper.getMonthAndYear(transactionState.date)
        val transaction = TransactionState(
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
            isLocked = false,
            isSpecialCase = false
        )

        repository.createIncomeTransaction(transaction)
        return DatabaseResultState.CreateTransactionSuccess
    }
}
