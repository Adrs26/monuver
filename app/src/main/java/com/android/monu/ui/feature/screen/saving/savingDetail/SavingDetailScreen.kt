package com.android.monu.ui.feature.screen.saving.savingDetail

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
import com.android.monu.R
import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.DeleteSavingState
import com.android.monu.ui.feature.screen.saving.savingDetail.components.CompleteSavingDialog
import com.android.monu.ui.feature.screen.saving.savingDetail.components.RemoveProgressDialog
import com.android.monu.ui.feature.screen.saving.savingDetail.components.RemoveSavingDialog
import com.android.monu.ui.feature.screen.saving.savingDetail.components.SavingDetailAppBar
import com.android.monu.ui.feature.screen.saving.savingDetail.components.SavingDetailBottomBar
import com.android.monu.ui.feature.screen.saving.savingDetail.components.SavingDetailContent
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.showMessageWithToast

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
        if (savingState.removeProgress is DeleteSavingState.Progress) {
            showRemoveProgressDialog = true

            if (savingState.removeProgress.current == savingState.removeProgress.total) {
                savingActions.onNavigateBack()
                context.getString(R.string.save_successfully_deleted).showMessageWithToast(context)
            }
        }
    }

    LaunchedEffect(savingState.completeResult) {
        savingState.completeResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.CompleteSavingSuccess) {
                savingActions.onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            SavingDetailAppBar(
                title = stringResource(R.string.save_detail),
                isActive = savingState.saving.isActive,
                onNavigateBack = savingActions::onNavigateBack,
                onNavigateToEditSaving = { savingActions.onNavigateToEditSaving(savingState.saving.id) },
                onRemoveSaving = { showRemoveConfirmationDialog = true }
            )
        },
        bottomBar = {
            if (savingState.saving.isActive) {
                SavingDetailBottomBar { showCompleteConfirmationDialog = true }
            }
        }
    ) { innerPadding ->
        SavingDetailContent(
            savingState = savingState.saving,
            transactions = savingState.transactions,
            onNavigateToDeposit = savingActions::onNavigateToDeposit,
            onNavigateToWithdraw = savingActions::onNavigateToWithdraw,
            onNavigateToTransactionDetail = savingActions::onNavigateToTransactionDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showRemoveConfirmationDialog) {
        RemoveSavingDialog(
            onDismissRequest = { showRemoveConfirmationDialog = false },
            onRemoveSaving = {
                showRemoveConfirmationDialog = false
                savingActions.onRemoveSaving(savingState.saving.id)
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
        CompleteSavingDialog(
            onDismissRequest = { showCompleteConfirmationDialog = false },
            onCompleteSaving = {
                showCompleteConfirmationDialog = false
                savingActions.onCompleteSaving(savingState.saving)
            }
        )
    }
}

data class SavingDetailState(
    val saving: Saving,
    val transactions: List<Transaction>,
    val removeProgress: DeleteSavingState,
    val completeResult: DatabaseResultMessage?
)

interface SavingDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditSaving(savingId: Long)

    fun onRemoveSaving(savingId : Long)
    fun onNavigateToDeposit()
    fun onNavigateToWithdraw()
    fun onNavigateToTransactionDetail(transactionId: Long)
    fun onCompleteSaving(saving: Saving)
}