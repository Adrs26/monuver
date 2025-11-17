package com.android.monuver.feature.transaction.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.feature.transaction.domain.model.EditTransactionState
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class UpdateIncomeTransactionUseCase(
    private val repository: TransactionRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(transactionState: EditTransactionState): DatabaseResultState {
        when {
            transactionState.title.isEmpty() -> return DatabaseResultState.EmptyTransactionTitle
            transactionState.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
        }

        val (month, year) = DateHelper.getMonthAndYear(transactionState.date)
        val transaction = TransactionState(
            id = transactionState.id,
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
            isLocked = transactionState.isLocked,
            isSpecialCase = false
        )

        repository.updateIncomeTransaction(
            transactionState = transaction,
            initialAmount = transactionState.initialAmount
        )
        return DatabaseResultState.UpdateTransactionSuccess
    }
}