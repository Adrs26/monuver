package com.android.monu.domain.usecase.budget

import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.presentation.utils.Cycle
import kotlinx.coroutines.flow.first
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class HandleExpiredBudgetUseCase(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke() {
        val budgets = repository.getAllActiveBudgets().first()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()

        budgets.forEach { budget ->
            if (isBudgetExpired(budget.endDate, formatter, today)) {
                repository.updateBudgetStatusToInactive(budget.category)

                if (budget.isAutoUpdate) {
                    var newStartDate = LocalDate.parse(budget.startDate, formatter)
                    var newEndDate = LocalDate.parse(budget.endDate, formatter)

                    while (newEndDate.isBefore(today)) {
                        newStartDate = shiftDate(budget.period, newStartDate, true)
                        newEndDate = shiftDate(budget.period, newEndDate, false)
                    }

                    if (!newEndDate.isBefore(today)) {
                        val newBudget = Budget(
                            category = budget.category,
                            period = budget.period,
                            startDate = newStartDate.format(formatter),
                            endDate = newEndDate.format(formatter),
                            maxAmount = budget.maxAmount,
                            usedAmount = 0L,
                            isActive = true,
                            isOverflowAllowed = budget.isOverflowAllowed,
                            isAutoUpdate = true,
                        )
                        repository.createNewBudget(newBudget)
                    }
                }
            }
        }
    }

    private fun isBudgetExpired(
        endDate: String,
        formatter: DateTimeFormatter,
        today: LocalDate
    ): Boolean {
        val end = LocalDate.parse(endDate, formatter)
        return end.isBefore(today)
    }

    private fun shiftDate(period: Int, date: LocalDate, isStartDate: Boolean): LocalDate {
        return when (period) {
            Cycle.MONTHLY -> {
                val shifted = date.plusMonths(1)
                if (isStartDate) shifted else shifted.withDayOfMonth(shifted.lengthOfMonth())
            }
            Cycle.WEEKLY -> date.plusWeeks(1)
            else -> date
        }
    }
}
