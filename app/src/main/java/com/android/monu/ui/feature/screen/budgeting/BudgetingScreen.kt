package com.android.monu.ui.feature.screen.budgeting

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.android.monu.domain.model.budget.Budget
import com.android.monu.ui.feature.screen.budgeting.components.BudgetAppBar
import com.android.monu.ui.feature.screen.budgeting.components.BudgetingContent

@Composable
fun BudgetingScreen(
    budgetState: BudgetState,
    budgetActions: BudgetActions
) {
    LaunchedEffect(Unit) {
        budgetActions.onHandleExpiredBudget()
    }

    Scaffold(
        topBar = {
            BudgetAppBar(
                onNavigateToInactiveBudget = budgetActions::onNavigateToInactiveBudget
            )
        }
    ) { innerPadding ->
        BudgetingContent(
            totalMaxAmount = budgetState.totalMaxAmount,
            totalUsedAmount = budgetState.totalUsedAmount,
            budgets = budgetState.budgets,
            onNavigateToBudgetDetail = budgetActions::onNavigateToBudgetDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

data class BudgetState(
    val totalMaxAmount: Long,
    val totalUsedAmount: Long,
    val budgets: List<Budget>,
)

interface BudgetActions {
    fun onNavigateToInactiveBudget()
    fun onNavigateToBudgetDetail(budgetId: Long)
    fun onHandleExpiredBudget()
}