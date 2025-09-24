package com.android.monu.ui.feature.screen.transaction

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
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.ui.feature.screen.transaction.components.TransactionFilterDialog
import com.android.monu.ui.feature.screen.transaction.components.TransactionFilterState
import com.android.monu.ui.feature.screen.transaction.components.TransactionList
import com.android.monu.ui.feature.screen.transaction.components.TransactionTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    transactionState: TransactionState,
    transactionActions: TransactionActions
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    val transactionFilterState = TransactionFilterState(
        yearFilterOptions = transactionState.yearFilterOptions,
        typeFilter = transactionState.typeFilter,
        yearFilter = transactionState.yearFilter,
        monthFilter = transactionState.monthFilter
    )

    LaunchedEffect(showFilterDialog) {
        if (showFilterDialog) {
            transactionActions.onYearFilterOptionsRequest()
        }
    }

    Scaffold(
        topBar = {
            TransactionTopBar(
                query = transactionState.queryFilter,
                onQueryChange = transactionActions::onQueryChange,
                modifier = Modifier
                    .padding(start = 16.dp, end = 12.dp, top = 48.dp, bottom = 12.dp),
                onFilterButtonClick = { showFilterDialog = true }
            )
        }
    ) { innerPadding ->
        TransactionList(
            transactions = transactionState.transactions,
            onNavigateToTransactionDetail = { transactionId ->
                transactionActions.onNavigateToTransactionDetail(transactionId)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showFilterDialog) {
        TransactionFilterDialog(
            filterState = transactionFilterState,
            onDismissRequest = { showFilterDialog = false },
            onFilterApply = transactionActions::onFilterApply
        )
    }
}

data class TransactionState(
    val queryFilter: String,
    val typeFilter: Int?,
    val yearFilter: Int?,
    val monthFilter: Int?,
    val yearFilterOptions: List<Int>,
    val transactions: LazyPagingItems<Transaction>,
)

interface TransactionActions {
    fun onQueryChange(query: String)
    fun onYearFilterOptionsRequest()
    fun onFilterApply(type: Int?, year: Int?, month: Int?)
    fun onNavigateToTransactionDetail(transactionId: Long)
}