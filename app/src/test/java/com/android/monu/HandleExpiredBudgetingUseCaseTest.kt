package com.android.monu

import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.usecase.budget.HandleExpiredBudgetUseCase
import com.android.monu.presentation.utils.Cycle
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.TimeZone

class HandleExpiredBudgetingUseCaseTest {

    private lateinit var repository: BudgetRepository
    private lateinit var useCase: HandleExpiredBudgetUseCase

    @Before
    fun setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        repository = mock<BudgetRepository>()
        useCase = HandleExpiredBudgetUseCase(repository)
    }

    @Test
    fun `expired budget should be marked inactive`() = runTest {
        val budget = Budget(
            id = 1,
            category = 1,
            cycle = Cycle.MONTHLY,
            startDate = "2025-07-01",
            endDate = "2025-07-31",
            maxAmount = 1_000_000,
            usedAmount = 500_000,
            isActive = true,
            isOverflowAllowed = false,
            isAutoUpdate = false
        )
        `when`(repository.getAllActiveBudgets()).thenReturn(flowOf(listOf(budget)))

        useCase()

        verify(repository).updateBudgetStatusToInactive(1)
        verify(repository, never()).createNewBudget(any())
    }

    @Test
    fun `expired budget with autoUpdate should create new budget`() = runTest {
        val budget = Budget(
            id = 1,
            category = 1,
            cycle = Cycle.MONTHLY,
            startDate = "2024-07-01",
            endDate = "2024-07-31",
            maxAmount = 500_000,
            usedAmount = 100_000,
            isActive = true,
            isOverflowAllowed = true,
            isAutoUpdate = true
        )
        `when`(repository.getAllActiveBudgets()).thenReturn(flowOf(listOf(budget)))

        useCase()

        verify(repository).updateBudgetStatusToInactive(1)
        val captor = argumentCaptor<Budget>()
        verify(repository).createNewBudget(captor.capture())

        assertEquals(captor.firstValue.usedAmount, 0L)
        assertNotEquals(captor.firstValue.startDate, budget.startDate)
        assertNotEquals(captor.firstValue.endDate, budget.endDate)
        assertEquals(captor.firstValue.category, 1)
    }

    @Test
    fun `non-expired budget should not be touched`() = runTest {
        val budget = Budget(
            id = 1,
            category = 1,
            cycle = Cycle.MONTHLY,
            startDate = "2024-08-01",
            endDate = "2099-12-31",
            maxAmount = 2_000_000,
            usedAmount = 100_000,
            isActive = true,
            isOverflowAllowed = false,
            isAutoUpdate = true
        )
        `when`(repository.getAllActiveBudgets()).thenReturn(flowOf(listOf(budget)))

        useCase()

        verify(repository, never()).updateBudgetStatusToInactive(any())
        verify(repository, never()).createNewBudget(any())
    }

    @Test
    fun `edge case - expired budget but next endDate still before today should create new budget with endDate after today`() = runTest {
        val budget = Budget(
            id = 1,
            category = 1,
            cycle = Cycle.MONTHLY,
            startDate = "2023-01-01",
            endDate = "2023-01-31",
            maxAmount = 500_000,
            usedAmount = 0,
            isActive = true,
            isOverflowAllowed = false,
            isAutoUpdate = true
        )
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()

        `when`(repository.getAllActiveBudgets()).thenReturn(flowOf(listOf(budget)))

        useCase()

        verify(repository).updateBudgetStatusToInactive(1)
        val captor = argumentCaptor<Budget>()
        verify(repository).createNewBudget(captor.capture())

        val newEndDate = LocalDate.parse(captor.firstValue.endDate, formatter)

        assertTrue(newEndDate.isAfter(today))
    }
}