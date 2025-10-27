package com.android.monu.ui.feature.screen.budgeting

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.android.monu.domain.model.BudgetState
import com.android.monu.ui.feature.screen.budgeting.components.BudgetAppBar
import com.android.monu.ui.feature.screen.budgeting.components.BudgetingContent

@Composable
fun BudgetingScreen(
    budgetUiState: BudgetUiState,
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
            totalMaxAmount = budgetUiState.totalMaxAmount,
            totalUsedAmount = budgetUiState.totalUsedAmount,
            budgets = budgetUiState.budgets,
            onNavigateToBudgetDetail = budgetActions::onNavigateToBudgetDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

data class BudgetUiState(
    val totalMaxAmount: Long,
    val totalUsedAmount: Long,
    val budgets: List<BudgetState>,
)

interface BudgetActions {
    fun onNavigateToInactiveBudget()
    fun onNavigateToBudgetDetail(budgetId: Long)
    fun onHandleExpiredBudget()
}