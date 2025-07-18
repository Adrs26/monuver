package com.android.monu.presentation.screen.transaction.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.android.monu.R
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.presentation.components.CategoryIcon
import com.android.monu.ui.theme.Blue800
import com.android.monu.ui.theme.Green600
import com.android.monu.ui.theme.Red600
import com.android.monu.presentation.utils.DatabaseCodeMapper
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.NumberFormatHelper

@Composable
fun TransactionList(
    transactions: LazyPagingItems<Transaction>,
    modifier: Modifier = Modifier,
    onNavigateToDetailTransaction: (Long) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (transactions.itemCount == 0 && transactions.loadState.refresh is LoadState.NotLoading) {
            Text(
                text = stringResource(R.string.no_transactions_yet),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.labelMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(
                    count = transactions.itemCount,
                    key = { index -> transactions[index]?.id!! }
                ) { index ->
                    transactions[index]?.let { transaction ->
                        TransactionListItem(
                            transaction = transaction,
                            modifier = Modifier
                                .animateItem()
                                .clickable { onNavigateToDetailTransaction(transaction.id) }
                                .padding(horizontal = 16.dp, vertical = 2.dp),
                        )
                    }
                }

                when (transactions.loadState.append) {
                    is LoadState.Loading -> {
                        item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                    }
                    is LoadState.Error -> {
                        val error = (transactions.loadState.append as LoadState.Error).error
                        item { Text("Error: ${error.localizedMessage}") }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun TransactionListItem(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(
                icon = DatabaseCodeMapper.toChildCategoryIcon(transaction.childCategory),
                backgroundColor = DatabaseCodeMapper
                    .toParentCategoryIconBackground(transaction.parentCategory),
                modifier = Modifier.size(40.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = transaction.title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${transaction.sourceName} · ${DateHelper.formatDateToReadable(transaction.date)}",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
            }
            Text(
                text = NumberFormatHelper.formatToRupiah(transaction.amount),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = transactionAmountColor(transaction.type)
                )
            )
        }
    }
}

private fun transactionAmountColor(type: Int): Color {
    return when (type) {
        1001 -> Green600
        1002 -> Red600
        else -> Blue800
    }
}

data class TransactionData(
    val id: Long,
    val title: String,
    val type: Int,
    val category: Int,
    val date: String,
    val amount: Long,
)