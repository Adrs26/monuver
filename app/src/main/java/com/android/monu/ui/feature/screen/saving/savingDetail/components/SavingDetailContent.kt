package com.android.monu.ui.feature.screen.saving.savingDetail.components

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
import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.ui.feature.components.CommonLottieAnimation
import com.android.monu.ui.feature.components.TransactionListItem
import com.android.monu.ui.feature.components.TransactionListItemState
import com.android.monu.ui.feature.utils.debouncedClickable

@Composable
fun SavingDetailContent(
    savingState: Saving,
    transactions: List<Transaction>,
    onNavigateToDeposit: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToTransactionDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when  {
        transactions.isEmpty() -> {
            SavingDetailEmptyListContent(
                savingState = savingState,
                onNavigateToDeposit = onNavigateToDeposit,
                onNavigateToWithdraw = onNavigateToWithdraw,
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
        else -> {
            SavingDetailListContent(
                savingState = savingState,
                transactions = transactions,
                onNavigateToDeposit = onNavigateToDeposit,
                onNavigateToWithdraw = onNavigateToWithdraw,
                onNavigateToTransactionDetail = onNavigateToTransactionDetail,
                modifier = modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun SavingDetailListContent(
    savingState: Saving,
    transactions: List<Transaction>,
    onNavigateToDeposit: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToTransactionDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            SavingDetailMainOverview(
                title = savingState.title,
                targetDate = savingState.targetDate,
                isActive = savingState.isActive,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            SavingDetailAmountOverview(
                currentAmount = savingState.currentAmount,
                targetAmount = savingState.targetAmount,
                modifier = Modifier.padding(16.dp)
            )
        }
        if (savingState.isActive) {
            item {
                SavingDetailButtonBar(
                    onNavigateToDeposit = onNavigateToDeposit,
                    onNavigateToWithdraw = onNavigateToWithdraw,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
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
            val transaction = transactions[index]
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
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                isDepositOrWithdraw = true
            )
        }
    }
}

@Composable
fun SavingDetailEmptyListContent(
    savingState: Saving,
    onNavigateToDeposit: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SavingDetailMainOverview(
            title = savingState.title,
            targetDate = savingState.targetDate,
            isActive = savingState.isActive,
        )
        SavingDetailAmountOverview(
            currentAmount = savingState.currentAmount,
            targetAmount = savingState.targetAmount,
            modifier = Modifier.padding(top = 8.dp)
        )
        if (savingState.isActive) {
            SavingDetailButtonBar(
                onNavigateToDeposit = onNavigateToDeposit,
                onNavigateToWithdraw = onNavigateToWithdraw,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
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