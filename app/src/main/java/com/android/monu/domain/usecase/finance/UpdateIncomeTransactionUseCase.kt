package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.screen.transaction.edittransaction.components.EditTransactionContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.DateHelper

class UpdateIncomeTransactionUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(transactionState: EditTransactionContentState): DatabaseResultMessage {
        when {
            transactionState.title.isEmpty() -> return DatabaseResultMessage.EmptyTransactionTitle
            transactionState.amount == 0L -> return DatabaseResultMessage.EmptyTransactionAmount
        }

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
            isLocked = transactionState.isLocked
        )

        repository.updateIncomeTransaction(
            transaction = transaction,
            initialAmount = transactionState.initialAmount
        )
        return DatabaseResultMessage.UpdateTransactionSuccess
    }
}