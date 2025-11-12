package com.android.monuver.ui.feature.screen.transaction

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.android.monuver.ui.feature.components.TransactionListItemState
import com.android.monuver.ui.feature.screen.transaction.components.TransactionFilterDialog
import com.android.monuver.ui.feature.screen.transaction.components.TransactionFilterState
import com.android.monuver.ui.feature.screen.transaction.components.TransactionListContent
import com.android.monuver.ui.feature.screen.transaction.components.TransactionTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    transactions: LazyPagingItems<TransactionListItemState>,
    searchQuery: String,
    filterState: TransactionFilterState,
    transactionActions: TransactionActions
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showFilterDialog) {
        if (showFilterDialog) {
            transactionActions.onYearFilterOptionsRequest()
        }
    }

    Scaffold(
        topBar = {
            TransactionTopBar(
                query = searchQuery,
                onQueryChange = transactionActions::onQueryChange,
                modifier = Modifier
                    .padding(start = 16.dp, end = 12.dp, top = 48.dp, bottom = 12.dp),
                onFilterButtonClick = { showFilterDialog = true }
            )
        }
    ) { innerPadding ->
        TransactionListContent(
            transactions = transactions,
            onNavigateToTransactionDetail = transactionActions::onNavigateToTransactionDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showFilterDialog) {
        TransactionFilterDialog(
            filterState = filterState,
            onDismissRequest = { showFilterDialog = false },
            onFilterApply = transactionActions::onFilterApply
        )
    }
}

interface TransactionActions {
    fun onQueryChange(query: String)
    fun onYearFilterOptionsRequest()
    fun onFilterApply(type: Int?, year: Int?, month: Int?)
    fun onNavigateToTransactionDetail(transactionId: Long)
}