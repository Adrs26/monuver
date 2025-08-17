package com.android.monu.domain.usecase.budgeting

import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.domain.repository.BudgetingRepository
import com.android.monu.presentation.utils.BudgetingPeriod
import kotlinx.coroutines.flow.first
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class HandleExpiredBudgetingUseCase(
    private val repository: BudgetingRepository
) {
    suspend operator fun invoke() {
        val budgets = repository.getAllActiveBudgets().first()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()

        budgets.forEach { budgeting ->
            if (isBudgetingExpired(budgeting.endDate, formatter, today)) {
                repository.updateBudgetingStatusToInactive(budgeting.category)

                if (budgeting.isAutoUpdate) {
                    var newStartDate = LocalDate.parse(budgeting.startDate, formatter)
                    var newEndDate = LocalDate.parse(budgeting.endDate, formatter)

                    while (newEndDate.isBefore(today)) {
                        newStartDate = shiftDate(budgeting.period, newStartDate, true)
                        newEndDate = shiftDate(budgeting.period, newEndDate, false)
                    }

                    if (!newEndDate.isBefore(today)) {
                        val newBudgeting = Budgeting(
                            category = budgeting.category,
                            period = budgeting.period,
                            startDate = newStartDate.format(formatter),
                            endDate = newEndDate.format(formatter),
                            maxAmount = budgeting.maxAmount,
                            usedAmount = 0L,
                            isActive = true,
                            isOverflowAllowed = budgeting.isOverflowAllowed,
                            isAutoUpdate = true,
                        )
                        repository.createNewBudgeting(newBudgeting)
                    }
                }
            }
        }
    }

    private fun isBudgetingExpired(
        endDate: String,
        formatter: DateTimeFormatter,
        today: LocalDate
    ): Boolean {
        val end = LocalDate.parse(endDate, formatter)
        return end.isBefore(today)
    }

    private fun shiftDate(period: Int, date: LocalDate, isStartDate: Boolean): LocalDate {
        return when (period) {
            BudgetingPeriod.MONTHLY -> {
                val shifted = date.plusMonths(1)
                if (isStartDate) shifted else shifted.withDayOfMonth(shifted.lengthOfMonth())
            }
            BudgetingPeriod.WEEKLY -> date.plusWeeks(1)
            else -> date
        }
    }

}
