package com.android.monu.domain.usecase.transaction

import com.android.monu.domain.model.TransactionDailySummaryState
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.utils.DateHelper
import com.android.monu.utils.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDate

class GetTransactionSummaryUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(month: Int, year: Int, week: Int): Flow<List<TransactionDailySummaryState>> {
        val (startDate, endDate) = DateHelper.getDateRangeForWeek(week, month, year)
        val transactionsFlow = repository.getTransactionsInRange(
            startDate.toString(),
            endDate.toString()
        )

        return transactionsFlow.map { transactions ->
            val grouped = transactions.groupBy { LocalDate.parse(it.date) }
            val result = mutableListOf<TransactionDailySummaryState>()

            var currentDate = startDate
            while (!currentDate.isAfter(endDate)) {
                val currentDateTransactions = grouped[currentDate] ?: emptyList()

                val (income, expense) = currentDateTransactions.fold(0L to 0L) { acc, transaction ->
                    when (transaction.type) {
                        TransactionType.INCOME -> acc.copy(first = acc.first + transaction.amount)
                        TransactionType.EXPENSE -> acc.copy(second = acc.second + transaction.amount)
                        else -> acc
                    }
                }

                result.add(
                    TransactionDailySummaryState(
                        date = currentDate.toString(),
                        totalIncome = income,
                        totalExpense = expense
                    )
                )

                currentDate = currentDate.plusDays(1)
            }

            result
        }
    }
}