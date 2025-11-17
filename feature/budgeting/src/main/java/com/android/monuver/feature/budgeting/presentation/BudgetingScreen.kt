package com.android.monuver.feature.budgeting.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.android.monuver.feature.budgeting.domain.model.BudgetListItemState

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
            _root_ide_package_.com.android.monuver.feature.budgeting.presentation.components.BudgetAppBar(
                onNavigateToInactiveBudget = budgetActions::onNavigateToInactiveBudget
            )
        }
    ) { innerPadding ->
        _root_ide_package_.com.android.monuver.feature.budgeting.presentation.components.BudgetingContent(
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