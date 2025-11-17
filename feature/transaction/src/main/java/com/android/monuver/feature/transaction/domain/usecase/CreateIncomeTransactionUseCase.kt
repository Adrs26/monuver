package com.android.monuver.feature.transaction.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.feature.transaction.domain.model.AddTransactionState
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateIncomeTransactionUseCase(
    private val repository: TransactionRepository
) {
    @OptIn(ExperimentalTime::class)
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
            timeStamp = Clock.System.now().toEpochMilliseconds(),
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