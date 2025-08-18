package com.android.monu.presentation.screen.budgeting

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.monu.domain.model.budget.Budget
import com.android.monu.presentation.screen.budgeting.components.BudgetAppBar
import com.android.monu.presentation.screen.budgeting.components.BudgetingContent

@Composable
fun BudgetingScreen(
    budgetState: BudgetState,
    onNavigateToInactiveBudget: () -> Unit,
    onNavigateToBudgetDetail: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            BudgetAppBar(
                onNavigateToInactiveBudget = onNavigateToInactiveBudget
            )
        }
    ) { innerPadding ->
        BudgetingContent(
            totalMaxAmount = budgetState.totalMaxAmount,
            totalUsedAmount = budgetState.totalUsedAmount,
            budgets = budgetState.budgets,
            onNavigateToBudgetDetail = onNavigateToBudgetDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

data class BudgetState(
    val totalMaxAmount: Long,
    val totalUsedAmount: Long,
    val budgets: List<Budget>,
)