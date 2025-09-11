package com.android.monu.ui.feature.screen.home.components

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
import com.android.monu.R
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.ui.feature.components.TransactionListItem
import com.android.monu.ui.feature.components.TransactionListItemState
import com.android.monu.ui.feature.utils.debouncedClickable

@Composable
fun HomeRecentTransactions(
    recentTransactions: List<Transaction>,
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
fun RecentTransactionList(
    recentTransactions: List<Transaction>,
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
                recentTransactions.forEach { transaction ->
                    val transactionState = TransactionListItemState(
                        id = transaction.id,
                        title = transaction.title,
                        type = transaction.type,
                        parentCategory = transaction.parentCategory,
                        childCategory = transaction.childCategory,
                        date = transaction.date,
                        amount = transaction.amount,
                        sourceName = transaction.sourceName,
                        isLocked = transaction.isLocked
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
    }
}
