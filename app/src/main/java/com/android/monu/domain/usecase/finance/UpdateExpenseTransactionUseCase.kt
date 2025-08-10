package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BudgetingRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.screen.transaction.edittransaction.components.EditTransactionContentState
import com.android.monu.presentation.utils.DateHelper

class UpdateExpenseTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository,
    private val budgetingRepository: BudgetingRepository
) {
    suspend operator fun invoke(transactionState: EditTransactionContentState): Result<Int> {
        return runCatching {
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
                sourceName = transactionState.sourceName
            )

            val accountBalance = accountRepository.getAccountBalance(transaction.sourceId)
            val difference = transaction.amount - transactionState.startAmount

            val oldBudgeting = budgetingRepository.getBudgetingForDate(
                category = transactionState.startParentCategory,
                date = transactionState.startDate
            )
            val newBudgeting = budgetingRepository.getBudgetingForDate(
                category = transaction.parentCategory,
                date = transaction.date
            )

            require(accountBalance != null && accountBalance >= difference) {
                "Saldo akun tidak mencukupi"
            }

            val budgetingStatus = getBudgetingStatus(oldBudgeting, newBudgeting)

            when (budgetingStatus) {
                NO_OLD_BUDGETING -> require(
                    newBudgeting?.usedAmount?.plus(transaction.amount)!! <= newBudgeting.maxAmount ||
                    newBudgeting.isOverflowAllowed
                ) { "Budget melebihi batas maksimum" }
                SAME_BUDGETING -> require(
                    newBudgeting?.usedAmount?.plus(difference)!! <= newBudgeting.maxAmount ||
                    newBudgeting.isOverflowAllowed
                ) { "Budget melebihi batas maksimum" }
                DIFFERENT_BUDGETING -> require(
                    newBudgeting?.usedAmount?.plus(transaction.amount)!! <= newBudgeting.maxAmount ||
                    newBudgeting.isOverflowAllowed
                ) { "Budget melebihi batas maksimum" }
            }

            financeRepository.updateExpenseTransaction(
                transaction = transaction,
                initialParentCategory = transactionState.startParentCategory,
                initialDate = transactionState.startDate,
                initialAmount = transactionState.startAmount,
                budgetingStatus = budgetingStatus
            )
        }
    }

    private fun getBudgetingStatus(oldBudgeting: Budgeting?, newBudgeting: Budgeting?): String {
        return when {
            oldBudgeting == null && newBudgeting != null -> NO_OLD_BUDGETING
            oldBudgeting != null && newBudgeting == null -> NO_NEW_BUDGETING
            oldBudgeting == null && newBudgeting == null -> NO_BUDGETING
            oldBudgeting?.category == newBudgeting?.category -> SAME_BUDGETING
            else -> DIFFERENT_BUDGETING
        }
    }

    companion object {
        const val NO_OLD_BUDGETING = "Old budgeting is null"
        const val NO_NEW_BUDGETING = "New budgeting is null"
        const val NO_BUDGETING = "No budgeting"
        const val SAME_BUDGETING = "Old and new budgeting are the same"
        const val DIFFERENT_BUDGETING = "Old and new budgeting are different"
    }
}
