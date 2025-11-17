package com.android.monuver.feature.budgeting.presentation.inactiveBudget

import androidx.compose.foundation.clickable
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
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.CommonLottieAnimation
import com.android.monuver.feature.budgeting.R
import com.android.monuver.feature.budgeting.domain.model.BudgetListItemState
import com.android.monuver.feature.budgeting.presentation.components.BudgetListItem

@Composable
internal fun InactiveBudgetScreen(
    budgets: LazyPagingItems<BudgetListItemState>,
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
        if (budgets.itemCount == 0 && budgets.loadState.refresh is LoadState.NotLoading) {
            CommonLottieAnimation(
                lottieAnimation = R.raw.empty,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(
                    count = budgets.itemCount,
                    key = { index -> budgets[index]?.id!! }
                ) { index ->
                    budgets[index]?.let { budgetState ->
                        BudgetListItem(
                            budgetState = budgetState,
                            modifier = Modifier.clickable { onNavigateToBudgetDetail(budgetState.id) }
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
