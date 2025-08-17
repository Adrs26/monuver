package com.android.monu.presentation.screen.budgeting

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.presentation.screen.budgeting.components.BudgetingAppBar
import com.android.monu.presentation.screen.budgeting.components.BudgetingContent

@Composable
fun BudgetingScreen(
    budgetingState: BudgetingState,
    onNavigateToInactiveBudgeting: () -> Unit,
    onItemClick: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            BudgetingAppBar(
                onHistoryClick = onNavigateToInactiveBudgeting
            )
        }
    ) { innerPadding ->
        BudgetingContent(
            totalMaxAmount = budgetingState.totalMaxAmount,
            totalUsedAmount = budgetingState.totalUsedAmount,
            budgets = budgetingState.budgets,
            onItemClick = onItemClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

data class BudgetingState(
    val totalMaxAmount: Long,
    val totalUsedAmount: Long,
    val budgets: List<Budgeting>,
)