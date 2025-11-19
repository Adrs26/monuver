package com.android.monuver.feature.analytics

import app.cash.turbine.test
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.util.TransactionChildCategory
import com.android.monuver.core.domain.util.TransactionParentCategory
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.feature.analytics.domain.repository.AnalyticsRepository
import com.android.monuver.feature.analytics.domain.usecase.GetAllTransactionsByCategoryUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetAllTransactionsByCategoryUseCaseTest {

    private lateinit var repository: AnalyticsRepository
    private lateinit var getAllTransactionsByCategoryUseCase: GetAllTransactionsByCategoryUseCase

    @Before
    fun setup() {
        repository = mock(AnalyticsRepository::class.java)
        getAllTransactionsByCategoryUseCase = GetAllTransactionsByCategoryUseCase(repository)
    }

    @Test
    fun `should emit list of transaction by category`() = runTest {
        val expected = listOf(
            TransactionState(
                title = "Makan Siang",
                type = TransactionType.EXPENSE,
                parentCategory = TransactionParentCategory.FOOD_BEVERAGES,
                childCategory = TransactionChildCategory.FOOD,
                date = "2025-11-19",
                month = 11,
                year = 2025,
                timeStamp = 123456789L,
                amount = 200_000,
                sourceId = 1,
                sourceName = "BCA",
                isLocked = false,
                isSpecialCase = false
            )
        )

        whenever(
            repository.getTransactionsByParentCategoryAndMonthAndYear(
                category = eq(TransactionParentCategory.FOOD_BEVERAGES),
                month = eq(11),
                year = eq(2025)
            )
        ).thenReturn(flowOf(expected))

        val flow = getAllTransactionsByCategoryUseCase(
            category = TransactionParentCategory.FOOD_BEVERAGES,
            month = 11,
            year = 2025
        )

        flow.test {
            val result = awaitItem()
            assertThat(result).containsExactlyElementsIn(expected)
            awaitComplete()
        }

        verify(repository)
            .getTransactionsByParentCategoryAndMonthAndYear(
                category = eq(TransactionParentCategory.FOOD_BEVERAGES),
                month = eq(11),
                year = eq(2025)
            )
    }
}