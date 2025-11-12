package com.android.monuver.ui.feature.screen.budgeting

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.android.monuver.ui.feature.screen.budgeting.components.BudgetAppBar
import com.android.monuver.ui.feature.screen.budgeting.components.BudgetListItemState
import com.android.monuver.ui.feature.screen.budgeting.components.BudgetingContent

@Composable
fun BudgetingScreen(
    totalMaxAmount: Long,
    totalUsedAmount: Long,
    budgets: List<BudgetListItemState>,
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
            totalMaxAmount = totalMaxAmount,
            totalUsedAmount = totalUsedAmount,
            budgets = budgets,
            onNavigateToBudgetDetail = budgetActions::onNavigateToBudgetDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

interface BudgetActions {
    fun onNavigateToInactiveBudget()
    fun onNavigateToBudgetDetail(budgetId: Long)
    fun onHandleExpiredBudget()
}