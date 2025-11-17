package com.android.monuver.feature.saving.presentation.savingDetail

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
import androidx.compose.ui.res.stringResource
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.core.domain.model.TransactionListItemState
import com.android.monuver.core.presentation.components.ConfirmationDialog
import com.android.monuver.core.presentation.util.isCompleteSavingSuccess
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.feature.saving.R
import com.android.monuver.feature.saving.domain.common.DeleteSavingStatusState
import com.android.monuver.feature.saving.presentation.savingDetail.components.RemoveProgressDialog
import com.android.monuver.feature.saving.presentation.savingDetail.components.SavingDetailAppBar
import com.android.monuver.feature.saving.presentation.savingDetail.components.SavingDetailBottomBar
import com.android.monuver.feature.saving.presentation.savingDetail.components.SavingDetailContent

@Composable
internal fun SavingDetailScreen(
    savingState: SavingState,
    transactions: List<TransactionListItemState>,
    progress: DeleteSavingStatusState,
    result: DatabaseResultState?,
    savingActions: SavingDetailActions
) {
    val context = LocalContext.current

    var showRemoveConfirmationDialog by remember { mutableStateOf(false) }
    var showRemoveProgressDialog by remember { mutableStateOf(false) }
    var showCompleteConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(progress) {
        if (progress is DeleteSavingStatusState.Progress) {
            showRemoveProgressDialog = true

            if (progress.current == progress.total) {
                savingActions.onNavigateBack()
                context.getString(R.string.save_successfully_deleted).showMessageWithToast(context)
            }
        }
    }

    LaunchedEffect(result) {
        result?.let { result ->
            result.showToast(context)
            if (result.isCompleteSavingSuccess()) savingActions.onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            SavingDetailAppBar(
                isActive = savingState.isActive,
                onNavigateBack = savingActions::onNavigateBack,
                onNavigateToEditSaving = { savingActions.onNavigateToEditSaving(savingState.id) },
                onRemoveSaving = { showRemoveConfirmationDialog = true }
            )
        },
        bottomBar = {
            if (savingState.isActive) {
                SavingDetailBottomBar { showCompleteConfirmationDialog = true }
            }
        }
    ) { innerPadding ->
        SavingDetailContent(
            savingState = savingState,
            transactions = transactions,
            onNavigateToDeposit = savingActions::onNavigateToDeposit,
            onNavigateToWithdraw = savingActions::onNavigateToWithdraw,
            onNavigateToTransactionDetail = savingActions::onNavigateToTransactionDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showRemoveConfirmationDialog) {
        ConfirmationDialog(
            text = stringResource(R.string.delete_save_confirmation),
            onDismissRequest = { showRemoveConfirmationDialog = false },
            onConfirmRequest = {
                showRemoveConfirmationDialog = false
                savingActions.onRemoveSaving(savingState.id)
                if (transactions.isEmpty()) {
                    savingActions.onNavigateBack()
                    context.getString(R.string.save_successfully_deleted).showMessageWithToast(context)
                }
            }
        )
    }

    if (showRemoveProgressDialog) {
        RemoveProgressDialog(
            onDismissRequest = { showRemoveProgressDialog = false }
        )
    }

    if (showCompleteConfirmationDialog) {
        ConfirmationDialog(
            text = stringResource(R.string.complete_save_confirmation),
            onDismissRequest = { showCompleteConfirmationDialog = false },
            onConfirmRequest = {
                showCompleteConfirmationDialog = false
                savingActions.onCompleteSaving(savingState)
            }
        )
    }
}

internal interface SavingDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditSaving(savingId: Long)

    fun onRemoveSaving(savingId : Long)
    fun onNavigateToDeposit()
    fun onNavigateToWithdraw()
    fun onNavigateToTransactionDetail(transactionId: Long)
    fun onCompleteSaving(savingState: SavingState)
}