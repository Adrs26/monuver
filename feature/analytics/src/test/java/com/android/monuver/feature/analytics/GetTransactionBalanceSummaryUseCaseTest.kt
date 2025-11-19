package com.android.monuver.feature.analytics

import app.cash.turbine.test
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.feature.analytics.domain.repository.AnalyticsRepository
import com.android.monuver.feature.analytics.domain.usecase.GetTransactionBalanceSummaryUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetTransactionBalanceSummaryUseCaseTest {

    private lateinit var repository: AnalyticsRepository
    private lateinit var getTransactionBalanceSummaryUseCase: GetTransactionBalanceSummaryUseCase

    @Before
    fun setup() {
        repository = mock(AnalyticsRepository::class.java)
        getTransactionBalanceSummaryUseCase = GetTransactionBalanceSummaryUseCase(repository)
    }

    @Test
    fun `should emit transaction balance summary`() = runTest {
        whenever(repository.getTotalMonthlyTransactionAmount(
            type = eq(TransactionType.INCOME),
            month = eq(11),
            year = eq(2025)
        )).thenReturn(flowOf(2_000_000))

        whenever(repository.getTotalMonthlyTransactionAmount(
            type = eq(TransactionType.EXPENSE),
            month = eq(11),
            year = eq(2025)
        )).thenReturn(flowOf(1_600_000))

        whenever(repository.getAverageDailyTransactionAmountInMonth(
            type = eq(TransactionType.INCOME),
            month = eq(11),
            year = eq(2025)
        )).thenReturn(flowOf(50_000.0))

        whenever(repository.getAverageDailyTransactionAmountInMonth(
            type = eq(TransactionType.EXPENSE),
            month = eq(11),
            year = eq(2025)
        )).thenReturn(flowOf(40_000.0))

        val flow = getTransactionBalanceSummaryUseCase(
            month = 11,
            year = 2025
        )

        flow.test {
            val result = awaitItem()
            assertThat(result.totalIncomeAmount).isEqualTo(2_000_000)
            assertThat(result.totalExpenseAmount).isEqualTo(1_600_000)
            assertThat(result.averageIncomeAmount).isEqualTo(50_000.0)
            assertThat(result.averageExpenseAmount).isEqualTo(40_000.0)
            awaitComplete()
        }

        verify(repository)
            .getTotalMonthlyTransactionAmount(
                type = eq(TransactionType.INCOME),
                month = eq(11),
                year = eq(2025)
            )
        verify(repository)
            .getTotalMonthlyTransactionAmount(
                type = eq(TransactionType.EXPENSE),
                month = eq(11),
                year = eq(2025)
            )
        verify(repository)
            .getAverageDailyTransactionAmountInMonth(
                type = eq(TransactionType.INCOME),
                month = eq(11),
                year = eq(2025)
            )
        verify(repository)
            .getAverageDailyTransactionAmountInMonth(
                type = eq(TransactionType.EXPENSE),
                month = eq(11),
                year = eq(2025)
            )
    }
}