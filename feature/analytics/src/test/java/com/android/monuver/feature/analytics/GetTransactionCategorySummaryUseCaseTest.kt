package com.android.monuver.feature.analytics

import app.cash.turbine.test
import com.android.monuver.core.domain.util.TransactionParentCategory
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.feature.analytics.domain.model.TransactionCategorySummaryState
import com.android.monuver.feature.analytics.domain.repository.AnalyticsRepository
import com.android.monuver.feature.analytics.domain.usecase.GetTransactionCategorySummaryUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetTransactionCategorySummaryUseCaseTest {

    private lateinit var repository: AnalyticsRepository
    private lateinit var getTransactionCategorySummaryUseCase: GetTransactionCategorySummaryUseCase

    @Before
    fun setup() {
        repository = mock(AnalyticsRepository::class.java)
        getTransactionCategorySummaryUseCase = GetTransactionCategorySummaryUseCase(repository)
    }

    @Test
    fun `should emit transaction category summary`() = runTest {
        val expected = listOf(
            TransactionCategorySummaryState(
                parentCategory = TransactionParentCategory.FOOD_BEVERAGES,
                totalAmount = 1_000_000,
            )
        )

        whenever(
            repository.getGroupedMonthlyTransactionAmountByParentCategory(
                type = eq(TransactionType.EXPENSE),
                month = eq(11),
                year = eq(2025)
            )
        ).thenReturn(flowOf(expected))

        val flow = getTransactionCategorySummaryUseCase(
            type = TransactionType.EXPENSE,
            month = 11,
            year = 2025
        )

        flow.test {
            val result = awaitItem()
            assertThat(result).containsExactlyElementsIn(expected)
            awaitComplete()
        }

        verify(repository)
            .getGroupedMonthlyTransactionAmountByParentCategory(
                type = eq(TransactionType.EXPENSE),
                month = eq(11),
                year = eq(2025)
            )
    }
}