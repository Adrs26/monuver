package com.android.monuver.feature.analytics.data.repository

import com.android.monuver.core.data.database.dao.TransactionDao
import com.android.monuver.core.data.database.entity.projection.TransactionCategorySummaryEntity
import com.android.monuver.core.data.database.entity.projection.TransactionSummaryEntity
import com.android.monuver.core.data.mapper.toDomain
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.analytics.domain.model.TransactionCategorySummaryState
import com.android.monuver.feature.analytics.domain.model.TransactionSummaryState
import com.android.monuver.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AnalyticsRepositoryImpl(
    private val transactionDao: TransactionDao
) : AnalyticsRepository {

    override fun getTransactionsByParentCategoryAndMonthAndYear(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionState>> {
        return transactionDao.getTransactionsByParentCategoryAndMonthAndYear(
            category = category,
            month = month,
            year = year
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override fun getTotalMonthlyTransactionAmount(
        type: Int,
        month: Int,
        year: Int
    ): Flow<Long?> {
        return transactionDao.getTotalMonthlyTransactionAmount(
            type = type,
            month = month,
            year = year
        )
    }

    override fun getAverageDailyTransactionAmountInMonth(
        type: Int,
        month: Int,
        year: Int
    ): Flow<Double?> {
        return transactionDao.getAverageDailyTransactionAmountInMonth(
            type = type,
            month = month,
            year = year
        )
    }

    override fun getGroupedMonthlyTransactionAmountByParentCategory(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummaryState>> {
        return transactionDao.getGroupedMonthlyTransactionAmountByParentCategory(
            type = type,
            month = month,
            year = year
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override fun getTransactionsInRange(
        startDate: String,
        endDate: String
    ): Flow<List<TransactionSummaryState>> {
        return transactionDao.getTransactionsInRange(
            startDate = startDate,
            endDate = endDate
        ).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    private fun TransactionSummaryEntity.toDomain() = TransactionSummaryState(
        type = type,
        date = date,
        amount = amount
    )

    private fun TransactionCategorySummaryEntity.toDomain() = TransactionCategorySummaryState(
        parentCategory = parentCategory,
        totalAmount = totalAmount
    )
}