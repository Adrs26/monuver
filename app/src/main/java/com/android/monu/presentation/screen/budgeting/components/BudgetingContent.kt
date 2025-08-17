package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.domain.model.budgeting.Budgeting
import com.android.monu.presentation.components.CommonLottieAnimation

@Composable
fun BudgetingContent(
    totalMaxAmount: Long,
    totalUsedAmount: Long,
    budgets: List<Budgeting>,
    onNavigateToBudgetingDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when  {
        budgets.isEmpty() -> {
            BudgetingEmptyListContent(
                totalMaxAmount = totalMaxAmount,
                totalUsedAmount = totalUsedAmount,
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
        else -> {
            BudgetingListContent(
                totalMaxAmount = totalMaxAmount,
                totalUsedAmount = totalUsedAmount,
                budgets = budgets,
                onNavigateToBudgetingDetail = onNavigateToBudgetingDetail,
                modifier = modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun BudgetingListContent(
    totalMaxAmount: Long,
    totalUsedAmount: Long,
    budgets: List<Budgeting>,
    onNavigateToBudgetingDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            BudgetingOverview(
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
            val budgetingState = BudgetingListItemState(
                id = budgets[index].id,
                category = budgets[index].category,
                startDate = budgets[index].startDate,
                endDate = budgets[index].endDate,
                maxAmount = budgets[index].maxAmount,
                usedAmount = budgets[index].usedAmount
            )

            BudgetingListItem(
                budgetingState = budgetingState,
                modifier = Modifier
                    .clickable { onNavigateToBudgetingDetail(budgetingState.id) }
            )
        }
    }
}

@Composable
fun BudgetingEmptyListContent(
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
        BudgetingOverview(
            totalMaxAmount = totalMaxAmount,
            totalUsedAmount = totalUsedAmount
        )
        Text(
            text = stringResource(R.string.list_active_budgeting),
            modifier = Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CommonLottieAnimation(lottieAnimation = R.raw.empty)
        }
    }
}