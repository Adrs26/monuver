package com.android.monu.presentation.screen.budgeting.budgetingdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.presentation.components.TransactionListItem
import com.android.monu.presentation.components.TransactionListState
import com.android.monu.presentation.screen.budgeting.budgetingdetail.components.BudgetingDetailAppBar
import com.android.monu.presentation.screen.budgeting.budgetingdetail.components.BudgetingDetailOverview
import com.android.monu.presentation.utils.TransactionChildCategory
import com.android.monu.presentation.utils.TransactionParentCategory
import com.android.monu.presentation.utils.TransactionType
import com.android.monu.ui.theme.MonuTheme

@Composable
fun BudgetingDetailScreen() {
    val dummyTransaction = TransactionListState(
        id = 0,
        title = "Pembayaran tagihan air",
        type = TransactionType.EXPENSE,
        parentCategory = TransactionParentCategory.BILLS_UTILITIES,
        childCategory = TransactionChildCategory.WATER,
        date = "2025-07-29",
        amount = 1000000L,
        sourceName = "BCA"
    )

    Scaffold(
        topBar = {
            BudgetingDetailAppBar(
                title = "Air",
                onNavigateBack = {},
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            BudgetingDetailOverview(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = "Riwayat transaksi",
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp, start = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            TransactionListItem(
                transactionState = dummyTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            TransactionListItem(
                transactionState = dummyTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            TransactionListItem(
                transactionState = dummyTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            TransactionListItem(
                transactionState = dummyTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            TransactionListItem(
                transactionState = dummyTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetingDetailScreenPreview() {
    MonuTheme {
        BudgetingDetailScreen()
    }
}