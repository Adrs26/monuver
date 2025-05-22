package com.android.monu.domain.usecase

import com.android.monu.domain.model.TransactionOverview
import com.android.monu.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMonthlyTransactionOverviewUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(type: Int, year: Int): Flow<List<TransactionOverview>> {
        return when (type) {
            1, 2 -> generateCompleteTransactionOverview(
                repository.getMonthlyTransactionOverviewByType(type, year)
            )
            else -> generateCompleteTransactionOverview(
                repository.getMonthlyTransactionBalance(year)
            )
        }
    }

    private fun generateCompleteTransactionOverview(
        rawTransactionOverviewData: Flow<List<TransactionOverview>>
    ): Flow<List<TransactionOverview>> {
        return rawTransactionOverviewData.map { rawData ->
            if (rawData.isEmpty()) return@map emptyList()

            val map = rawData.associateBy { it.month to it.year }
            generateAllMonthsForYear(rawData[0].year).map { (month, year) ->
                map[month to year] ?: TransactionOverview(month, year, 0)
            }
        }
    }

    private fun generateAllMonthsForYear(year: Int): List<Pair<Int, Int>> {
        return (1..12).map { month -> month to year }
    }
}