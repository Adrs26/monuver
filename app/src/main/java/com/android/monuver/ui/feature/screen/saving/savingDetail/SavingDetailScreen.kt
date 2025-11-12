package com.android.monuver.ui.feature.screen.saving.savingDetail

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
import com.android.monuver.R
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.common.DeleteSavingStatusState
import com.android.monuver.domain.model.SavingState
import com.android.monuver.ui.feature.components.ConfirmationDialog
import com.android.monuver.ui.feature.components.TransactionListItemState
import com.android.monuver.ui.feature.screen.saving.savingDetail.components.RemoveProgressDialog
import com.android.monuver.ui.feature.screen.saving.savingDetail.components.SavingDetailAppBar
import com.android.monuver.ui.feature.screen.saving.savingDetail.components.SavingDetailBottomBar
import com.android.monuver.ui.feature.screen.saving.savingDetail.components.SavingDetailContent
import com.android.monuver.ui.feature.utils.isCompleteSavingSuccess
import com.android.monuver.ui.feature.utils.showMessageWithToast
import com.android.monuver.ui.feature.utils.showToast

@Composable
fun SavingDetailScreen(
    savingState: SavingDetailState,
    savingActions: SavingDetailActions
) {
    var showRemoveConfirmationDialog by remember { mutableStateOf(false) }
    var showRemoveProgressDialog by remember { mutableStateOf(false) }
    var showCompleteConfirmationDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(savingState.removeProgress) {
        if (savingState.removeProgress is DeleteSavingStatusState.Progress) {
            showRemoveProgressDialog = true

            if (savingState.removeProgress.current == savingState.removeProgress.total) {
                savingActions.onNavigateBack()
                context.getString(R.string.save_successfully_deleted).showMessageWithToast(context)
            }
        }
    }

    LaunchedEffect(savingState.completeResult) {
        savingState.completeResult?.let { result ->
            result.showToast(context)
            if (result.isCompleteSavingSuccess()) savingActions.onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            SavingDetailAppBar(
                isActive = savingState.savingState.isActive,
                onNavigateBack = savingActions::onNavigateBack,
                onNavigateToEditSaving = { savingActions.onNavigateToEditSaving(savingState.savingState.id) },
                onRemoveSaving = { showRemoveConfirmationDialog = true }
            )
        },
        bottomBar = {
            if (savingState.savingState.isActive) {
                SavingDetailBottomBar { showCompleteConfirmationDialog = true }
            }
        }
    ) { innerPadding ->
        SavingDetailContent(
            savingState = savingState.savingState,
            transactions = savingState.transactions,
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
                savingActions.onRemoveSaving(savingState.savingState.id)
                if (savingState.transactions.isEmpty()) {
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
                savingActions.onCompleteSaving(savingState.savingState)
            }
        )
    }
}

data class SavingDetailState(
    val savingState: SavingState,
    val transactions: List<TransactionListItemState>,
    val removeProgress: DeleteSavingStatusState,
    val completeResult: DatabaseResultState?
)

interface SavingDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditSaving(savingId: Long)

    fun onRemoveSaving(savingId : Long)
    fun onNavigateToDeposit()
    fun onNavigateToWithdraw()
    fun onNavigateToTransactionDetail(transactionId: Long)
    fun onCompleteSaving(savingState: SavingState)
}