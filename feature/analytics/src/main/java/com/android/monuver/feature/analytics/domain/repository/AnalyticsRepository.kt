package com.android.monuver.feature.analytics.domain.repository

import com.android.monuver.feature.analytics.domain.model.TransactionCategorySummaryState
import com.android.monuver.feature.analytics.domain.model.TransactionSummaryState
import com.android.monuver.core.domain.model.TransactionState
import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {

    fun getTransactionsByParentCategoryAndMonthAndYear(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionState>>

    fun getTotalMonthlyTransactionAmount(
        type: Int,
        month: Int,
        year: Int
    ): Flow<Long?>

    fun getAverageDailyTransactionAmountInMonth(
        type: Int,
        month: Int,
        year: Int
    ): Flow<Double?>

    fun getGroupedMonthlyTransactionAmountByParentCategory(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummaryState>>

    fun getTransactionsInRange(
        startDate: String,
        endDate: String
    ): Flow<List<TransactionSummaryState>>
}