package com.android.monu.presentation.screen.analytics.analyticscategorytransaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.components.TransactionListItem
import com.android.monu.presentation.components.TransactionListItemState
import com.android.monu.presentation.utils.DatabaseCodeMapper

@Composable
fun AnalyticsCategoryTransactionScreen(
    category: Int,
    transactions: List<Transaction>,
    onNavigateBack: () -> Unit,
    onNavigateToTransactionDetail: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(DatabaseCodeMapper.toParentCategoryTitle(category)),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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
                    sourceName = transactions[index].sourceName
                )

                TransactionListItem(
                    transactionState = transactionState,
                    modifier = Modifier
                        .clickable { onNavigateToTransactionDetail(transactionState.id) }
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                )
            }
        }
    }
}