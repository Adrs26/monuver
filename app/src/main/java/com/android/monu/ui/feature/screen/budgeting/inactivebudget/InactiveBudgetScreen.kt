package com.android.monu.ui.feature.screen.budgeting.inactivebudget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.android.monu.R
import com.android.monu.domain.model.budget.Budget
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.components.CommonLottieAnimation
import com.android.monu.ui.feature.screen.budgeting.components.BudgetListItem
import com.android.monu.ui.feature.screen.budgeting.components.BudgetListItemState

@Composable
fun InactiveBudgetScreen(
    budgets: LazyPagingItems<Budget>,
    onNavigateBack: () -> Unit,
    onNavigateToBudgetDetail: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.inactive_budget),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (budgets.itemCount == 0 && budgets.loadState.refresh is LoadState.NotLoading) {
                CommonLottieAnimation(lottieAnimation = R.raw.empty)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(
                        count = budgets.itemCount,
                        key = { index -> budgets[index]?.id!! }
                    ) { index ->
                        budgets[index]?.let { budget ->
                            val budgetState = BudgetListItemState(
                                id = budget.id,
                                category = budget.category,
                                startDate = budget.startDate,
                                endDate = budget.endDate,
                                maxAmount = budget.maxAmount,
                                usedAmount = budget.usedAmount
                            )

                            BudgetListItem(
                                budgetState = budgetState,
                                modifier = Modifier
                                    .clickable { onNavigateToBudgetDetail(budgetState.id) }
                            )
                        }
                    }

                    when (budgets.loadState.append) {
                        is LoadState.Loading -> {
                            item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                        }
                        is LoadState.Error -> {
                            val error = (budgets.loadState.append as LoadState.Error).error
                            item { Text("Error: ${error.localizedMessage}") }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
