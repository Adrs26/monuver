package com.android.monu.presentation.screen.transaction.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.android.monu.R
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.presentation.components.TransactionListItem
import com.android.monu.presentation.components.TransactionListItemState

@Composable
fun TransactionList(
    transactions: LazyPagingItems<Transaction>,
    modifier: Modifier = Modifier,
    onNavigateToTransactionDetail: (Long) -> Unit
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
                        val transactionState = TransactionListItemState(
                            id = transaction.id,
                            title = transaction.title,
                            type = transaction.type,
                            parentCategory = transaction.parentCategory,
                            childCategory = transaction.childCategory,
                            date = transaction.date,
                            amount = transaction.amount,
                            sourceName = transaction.sourceName
                        )

                        TransactionListItem(
                            transactionState = transactionState,
                            modifier = Modifier
                                .animateItem()
                                .clickable { onNavigateToTransactionDetail(transactionState.id) }
                                .padding(horizontal = 16.dp, vertical = 2.dp)
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