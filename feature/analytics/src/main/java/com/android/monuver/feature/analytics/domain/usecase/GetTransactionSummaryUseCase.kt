package com.android.monuver.feature.analytics.domain.usecase

import com.android.monuver.feature.analytics.domain.model.TransactionDailySummaryState
import com.android.monuver.feature.analytics.domain.repository.AnalyticsRepository
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class GetTransactionSummaryUseCase(
    private val repository: AnalyticsRepository
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
            while (currentDate <= endDate) {
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

                currentDate = currentDate.plus(DatePeriod(days = 1))
            }

            result
        }
    }
}