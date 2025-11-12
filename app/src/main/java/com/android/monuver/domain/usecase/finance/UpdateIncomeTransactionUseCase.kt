package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.EditTransactionState
import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.FinanceRepository
import com.android.monuver.utils.DateHelper

class UpdateIncomeTransactionUseCase(
    private val repository: FinanceRepository
) {
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
            timeStamp = System.currentTimeMillis(),
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