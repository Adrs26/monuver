package com.android.monuver.feature.transaction.presentation.transactionDetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.presentation.components.ConfirmationDialog
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.feature.transaction.R
import com.android.monuver.feature.transaction.presentation.transactionDetail.components.TransactionDetailAppBar
import com.android.monuver.feature.transaction.presentation.transactionDetail.components.TransactionDetailContent

@Composable
internal fun DetailTransactionScreen(
    transactionState: TransactionState,
    transactionActions: TransactionDetailActions
) {
    val context = LocalContext.current

    var showRemoveDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TransactionDetailAppBar(
                isTransactionLocked = transactionState.isLocked,
                onNavigateBack = transactionActions::onNavigateBack,
                onNavigateToEditTransaction = {
                    transactionActions.onNavigateToEditTransaction(
                        transactionId = transactionState.id,
                        transactionType = transactionState.type,
                        transactionCategory = transactionState.childCategory
                    )
                },
                onRemoveTransaction = { showRemoveDialog = true }
            )
        }
    ) { innerPadding ->
        TransactionDetailContent(
            transactionState = transactionState,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 32.dp)
        )
    }

    if (showRemoveDialog) {
        ConfirmationDialog(
            text = stringResource(R.string.delete_this_transaction),
            onDismissRequest = { showRemoveDialog = false },
            onConfirmRequest = {
                showRemoveDialog = false
                transactionActions.onRemoveTransaction(transactionState)
                context.getString(R.string.transaction_successfully_deleted).showMessageWithToast(context)
                transactionActions.onNavigateBack()
            }
        )
    }
}

internal interface TransactionDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditTransaction(
        transactionId: Long,
        transactionType: Int,
        transactionCategory: Int
    )
    fun onRemoveTransaction(transactionState: TransactionState)
}