package com.android.monuver.ui.feature.screen.transaction.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.android.monuver.R
import com.android.monuver.ui.feature.components.CommonLottieAnimation
import com.android.monuver.ui.feature.components.TransactionListItem
import com.android.monuver.ui.feature.components.TransactionListItemState
import com.android.monuver.ui.feature.utils.debouncedClickable

@Composable
fun TransactionListContent(
    transactions: LazyPagingItems<TransactionListItemState>,
    onNavigateToTransactionDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (transactions.itemCount == 0 && transactions.loadState.refresh is LoadState.NotLoading) {
        CommonLottieAnimation(
            lottieAnimation = R.raw.empty,
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(
                count = transactions.itemCount,
                key = { index -> transactions[index]?.id!! }
            ) { index ->
                transactions[index]?.let { transactionState ->
                    TransactionListItem(
                        transactionState = transactionState,
                        modifier = Modifier
                            .animateItem()
                            .debouncedClickable { onNavigateToTransactionDetail(transactionState.id) }
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