package com.android.monu.presentation.screen.transaction.transactiondetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.android.monu.R
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.presentation.screen.transaction.transactiondetail.components.DeleteTransactionDialog
import com.android.monu.presentation.screen.transaction.transactiondetail.components.TransactionDetailAppBar
import com.android.monu.presentation.screen.transaction.transactiondetail.components.TransactionDetailContent
import com.android.monu.presentation.utils.showMessageWithToast

@Composable
fun DetailTransactionScreen(
    transaction: Transaction,
    transactionActions: TransactionDetailActions
) {
    var showRemoveDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TransactionDetailAppBar(
                onNavigateBack = { transactionActions.onNavigateBack() },
                onNavigateToEditTransaction = {
                    transactionActions.onNavigateToEditTransaction(
                        transactionId = transaction.id,
                        transactionType = transaction.type,
                        transactionCategory = transaction.childCategory
                    )
                },
                onRemoveTransaction = { showRemoveDialog = true }
            )
        }
    ) { innerPadding ->
        TransactionDetailContent(
            transaction = transaction,
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showRemoveDialog) {
        DeleteTransactionDialog(
            onDismissRequest = { showRemoveDialog = false },
            onRemoveTransaction = {
                showRemoveDialog = false
                transactionActions.onRemoveTransaction(transaction)
                context.getString(R.string.transaction_successfully_deleted).showMessageWithToast(context)
                transactionActions.onNavigateBack()
            }
        )
    }
}

interface TransactionDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditTransaction(transactionId: Long, transactionType: Int, transactionCategory: Int)
    fun onRemoveTransaction(transaction: Transaction)
}