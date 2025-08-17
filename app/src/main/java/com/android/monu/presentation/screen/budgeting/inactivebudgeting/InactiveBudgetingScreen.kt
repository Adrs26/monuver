package com.android.monu.presentation.screen.budgeting.inactivebudgeting

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
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.android.monu.R
import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.components.CommonLottieAnimation
import com.android.monu.presentation.screen.budgeting.components.BudgetingListItem
import com.android.monu.presentation.screen.budgeting.components.BudgetingListItemState

@Composable
fun InactiveBudgetingScreen(
    budgets: LazyPagingItems<Budgeting>,
    onNavigateBack: () -> Unit,
    onNavigateToBudgetingDetail: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = "Budget selesai",
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
                            val budgetingState = BudgetingListItemState(
                                id = budget.id,
                                category = budget.category,
                                startDate = budget.startDate,
                                endDate = budget.endDate,
                                maxAmount = budget.maxAmount,
                                usedAmount = budget.usedAmount
                            )

                            BudgetingListItem(
                                budgetingState = budgetingState,
                                modifier = Modifier
                                    .clickable { onNavigateToBudgetingDetail(budgetingState.id) }
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
