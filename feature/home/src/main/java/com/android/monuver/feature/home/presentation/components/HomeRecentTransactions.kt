package com.android.monuver.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.core.domain.model.TransactionListItemState
import com.android.monuver.core.presentation.components.TransactionListItem
import com.android.monuver.core.presentation.util.debouncedClickable
import com.android.monuver.feature.home.R

@Composable
internal fun HomeRecentTransactions(
    recentTransactions: List<TransactionListItemState>,
    onNavigateToTransaction: () -> Unit,
    onNavigateToTransactionDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.recent_transactions),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.see_all),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .debouncedClickable { onNavigateToTransaction() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
            )
        }
        RecentTransactionList(
            recentTransactions = recentTransactions,
            onNavigateToTransactionDetail = onNavigateToTransactionDetail
        )
    }
}

@Composable
private fun RecentTransactionList(
    recentTransactions: List<TransactionListItemState>,
    onNavigateToTransactionDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (recentTransactions.isEmpty()) {
            Text(
                text = stringResource(R.string.no_transactions_yet),
                modifier = Modifier.padding(top = 48.dp, bottom = 40.dp),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
            )
        } else {
            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                recentTransactions.forEach { transactionState ->
                    TransactionListItem(
                        transactionState = transactionState,
                        modifier = Modifier
                            .debouncedClickable { onNavigateToTransactionDetail(transactionState.id) }
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
