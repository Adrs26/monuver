package com.android.monuver.feature.analytics.presentation.analyticsCategoryTransaction

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.model.TransactionListItemState
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.TransactionListItem
import com.android.monuver.core.presentation.util.DatabaseCodeMapper
import com.android.monuver.core.presentation.util.debouncedClickable

@Composable
internal fun AnalyticsCategoryTransactionScreen(
    category: Int,
    transactions: List<TransactionListItemState>,
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
                TransactionListItem(
                    transactionState = transactions[index],
                    modifier = Modifier
                        .debouncedClickable { onNavigateToTransactionDetail(transactions[index].id) }
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                )
            }
        }
    }
}