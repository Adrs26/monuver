package com.android.monuver.feature.analytics

import app.cash.turbine.test
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.feature.analytics.domain.model.TransactionDailySummaryState
import com.android.monuver.feature.analytics.domain.model.TransactionSummaryState
import com.android.monuver.feature.analytics.domain.repository.AnalyticsRepository
import com.android.monuver.feature.analytics.domain.usecase.GetTransactionSummaryUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetTransactionSummaryUseCaseTest {

    private lateinit var repository: AnalyticsRepository
    private lateinit var getTransactionSummaryUseCase: GetTransactionSummaryUseCase

    @Before
    fun setup() {
        repository = mock(AnalyticsRepository::class.java)
        getTransactionSummaryUseCase = GetTransactionSummaryUseCase(repository)
    }

    @Test
    fun `should emit transaction summary`() = runTest {
        val transactions = listOf(
            TransactionSummaryState(
                type = TransactionType.INCOME,
                date = "2025-11-18",
                amount = 300_000
            ),
            TransactionSummaryState(
                type = TransactionType.EXPENSE,
                date = "2025-11-19",
                amount = 200_000
            ),
            TransactionSummaryState(
                type = TransactionType.TRANSFER,
                date = "2025-11-19",
                amount = 200_000
            )
        )

        val expected = listOf(
            TransactionDailySummaryState(
                date = "2025-11-15",
                totalIncome = 0,
                totalExpense = 0,
            ),
            TransactionDailySummaryState(
                date = "2025-11-16",
                totalIncome = 0,
                totalExpense = 0,
            ),
            TransactionDailySummaryState(
                date = "2025-11-17",
                totalIncome = 0,
                totalExpense = 0,
            ),
            TransactionDailySummaryState(
                date = "2025-11-18",
                totalIncome = 300_000,
                totalExpense = 0,
            ),
            TransactionDailySummaryState(
                date = "2025-11-19",
                totalIncome = 0,
                totalExpense = 200_000,
            ),
            TransactionDailySummaryState(
                date = "2025-11-20",
                totalIncome = 0,
                totalExpense = 0,
            ),
            TransactionDailySummaryState(
                date = "2025-11-21",
                totalIncome = 0,
                totalExpense = 0,
            )
        )

        whenever(
            repository.getTransactionsInRange(
                startDate = eq("2025-11-15"),
                endDate = eq("2025-11-21")
            )
        ).thenReturn(flowOf(transactions))

        val flow = getTransactionSummaryUseCase(
            month = 11,
            year = 2025,
            week = 3
        )

        flow.test {
            val result = awaitItem()
            assertThat(result).containsExactlyElementsIn(expected)
            awaitComplete()
        }

        verify(repository).getTransactionsInRange(
            startDate = eq("2025-11-15"),
            endDate = eq("2025-11-21")
        )
    }
}