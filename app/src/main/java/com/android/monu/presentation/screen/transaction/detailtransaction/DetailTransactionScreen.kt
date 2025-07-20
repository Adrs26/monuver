package com.android.monu.presentation.screen.transaction.detailtransaction

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.android.monu.R
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.presentation.screen.transaction.detailtransaction.components.DeleteTransactionAppBar
import com.android.monu.presentation.screen.transaction.detailtransaction.components.DeleteTransactionDialog
import com.android.monu.presentation.screen.transaction.detailtransaction.components.DetailTransactionContent
import com.android.monu.presentation.utils.showMessageWithToast

@Composable
fun DetailTransactionScreen(
    transaction: Transaction,
    removeTransactionResult: Result<Int>?,
    transactionActions: DetailTransactionActions
) {
    var showRemoveDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(removeTransactionResult) {
        removeTransactionResult?.let {
            if (it.isSuccess) {
                context.getString(R.string.transaction_successfully_deleted)
                    .showMessageWithToast(context)
                transactionActions.onNavigateBack()
            } else {
                "Terjadi kesalahan saat menghapus transaksi".showMessageWithToast(context)
            }
        }
    }

    Scaffold(
        topBar = {
            DeleteTransactionAppBar(
                onNavigateBack = { transactionActions.onNavigateBack() },
                onEditClick = { transactionActions.onNavigateToEdit() },
                onDeleteClick = { showRemoveDialog = true }
            )
        }
    ) { innerPadding ->
        DetailTransactionContent(
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
            }
        )
    }
}

interface DetailTransactionActions {
    fun onNavigateBack()
    fun onNavigateToEdit()
    fun onRemoveTransaction(transaction: Transaction)
}