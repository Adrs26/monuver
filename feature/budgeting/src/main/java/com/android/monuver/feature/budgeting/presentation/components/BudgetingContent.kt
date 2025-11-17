package com.android.monuver.feature.budgeting.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monuver.core.presentation.components.CommonLottieAnimation
import com.android.monuver.core.presentation.util.debouncedClickable
import com.android.monuver.feature.budgeting.R
import com.android.monuver.feature.budgeting.domain.model.BudgetListItemState

@Composable
internal fun BudgetingContent(
    totalMaxAmount: Long,
    totalUsedAmount: Long,
    budgets: List<BudgetListItemState>,
    onNavigateToBudgetDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when  {
        budgets.isEmpty() -> {
            BudgetEmptyListContent(
                totalMaxAmount = totalMaxAmount,
                totalUsedAmount = totalUsedAmount,
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
        else -> {
            BudgetListContent(
                totalMaxAmount = totalMaxAmount,
                totalUsedAmount = totalUsedAmount,
                budgets = budgets,
                onNavigateToBudgetDetail = onNavigateToBudgetDetail,
                modifier = modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun BudgetListContent(
    totalMaxAmount: Long,
    totalUsedAmount: Long,
    budgets: List<BudgetListItemState>,
    onNavigateToBudgetDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            BudgetOverview(
                totalMaxAmount = totalMaxAmount,
                totalUsedAmount = totalUsedAmount,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Text(
                text = stringResource(R.string.list_active_budgeting),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(
            count = budgets.size,
            key = { index -> budgets[index].id }
        ) { index ->
            BudgetListItem(
                budgetState = budgets[index],
                modifier = Modifier.debouncedClickable { onNavigateToBudgetDetail(budgets[index].id) }
            )
        }
    }
}

@Composable
private fun BudgetEmptyListContent(
    totalMaxAmount: Long,
    totalUsedAmount: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BudgetOverview(
            totalMaxAmount = totalMaxAmount,
            totalUsedAmount = totalUsedAmount
        )
        Text(
            text = stringResource(R.string.list_active_budgeting),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        CommonLottieAnimation(lottieAnimation = R.raw.empty)
    }
}