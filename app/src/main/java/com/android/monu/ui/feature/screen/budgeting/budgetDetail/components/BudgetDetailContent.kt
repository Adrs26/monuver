package com.android.monu.ui.feature.screen.budgeting.budgetDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.android.monu.domain.model.BudgetState
import com.android.monu.domain.model.TransactionState
import com.android.monu.ui.feature.components.CommonLottieAnimation
import com.android.monu.ui.feature.components.TransactionListItem
import com.android.monu.ui.feature.components.TransactionListItemState
import com.android.monu.ui.feature.utils.debouncedClickable

@Composable
fun BudgetDetailContent(
    budgetState: BudgetState,
    transactions: List<TransactionState>,
    onNavigateToTransactionDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when  {
        transactions.isEmpty() -> {
            BudgetDetailEmptyListContent(
                budgetState = budgetState,
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
        else -> {
            BudgetDetailListContent(
                budgetState = budgetState,
                transactions = transactions,
                onNavigateToTransactionDetail = onNavigateToTransactionDetail,
                modifier = modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun BudgetDetailListContent(
    budgetState: BudgetState,
    transactions: List<TransactionState>,
    onNavigateToTransactionDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            BudgetDetailMainOverview(
                budgetState = budgetState,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            BudgetDetailAmountOverview(
                budgetState = budgetState,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
        }
        item {
            Text(
                text = stringResource(R.string.transaction_history),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(
            count = transactions.size,
            key = { index -> transactions[index].id }
        ) { index ->
            val transactionState = TransactionListItemState(
                id = transactions[index].id,
                title = transactions[index].title,
                type = transactions[index].type,
                parentCategory = transactions[index].parentCategory,
                childCategory = transactions[index].childCategory,
                date = transactions[index].date,
                amount = transactions[index].amount,
                sourceName = transactions[index].sourceName,
                isLocked = transactions[index].isLocked
            )

            TransactionListItem(
                transactionState = transactionState,
                modifier = Modifier
                    .debouncedClickable { onNavigateToTransactionDetail(transactionState.id) }
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun BudgetDetailEmptyListContent(
    budgetState: BudgetState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BudgetDetailMainOverview(budgetState = budgetState)
        BudgetDetailAmountOverview(
            budgetState = budgetState,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(R.string.transaction_history),
            modifier = Modifier.padding(top = 16.dp),
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