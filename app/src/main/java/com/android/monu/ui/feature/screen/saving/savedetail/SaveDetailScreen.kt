package com.android.monu.ui.feature.screen.saving.savedetail

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
import com.android.monu.domain.model.save.Save
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.DeleteSaveState
import com.android.monu.ui.feature.screen.saving.savedetail.components.DeleteSaveDialog
import com.android.monu.ui.feature.screen.saving.savedetail.components.RemoveProgressDialog
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailAppBar
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailBottomBar
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailContent
import com.android.monu.ui.feature.utils.showMessageWithToast

@Composable
fun SaveDetailScreen(
    save: Save,
    transactions: List<Transaction>,
    removeProgress: DeleteSaveState,
    saveActions: SaveDetailActions
) {
    var showRemoveConfirmationDialog by remember { mutableStateOf(false) }
    var showRemoveProgressDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(removeProgress) {
        if (removeProgress is DeleteSaveState.Progress) {
            showRemoveProgressDialog = true

            if (removeProgress.current == removeProgress.total) {
                saveActions.onNavigateBack()
                context.getString(R.string.save_successfully_deleted).showMessageWithToast(context)
            }
        }
    }

    Scaffold(
        topBar = {
            SaveDetailAppBar(
                title = stringResource(R.string.save_detail),
                isActive = save.isActive,
                onNavigateBack = saveActions::onNavigateBack,
                onNavigateToEditSave = { saveActions.onNavigateToEditSave(save.id) },
                onRemoveSave = { showRemoveConfirmationDialog = true }
            )
        },
        bottomBar = {
            SaveDetailBottomBar()
        }
    ) { innerPadding ->
        SaveDetailContent(
            saveState = save,
            transactions = transactions,
            onNavigateToDeposit = saveActions::onNavigateToDeposit,
            onNavigateToWithdraw = saveActions::onNavigateToWithdraw,
            onNavigateToTransactionDetail = saveActions::onNavigateToTransactionDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showRemoveConfirmationDialog) {
        DeleteSaveDialog(
            onDismissRequest = { showRemoveConfirmationDialog = false },
            onRemoveSave = {
                showRemoveConfirmationDialog = false
                saveActions.onRemoveSave(save.id)
            }
        )
    }

    if (showRemoveProgressDialog) {
        RemoveProgressDialog(
            onDismissRequest = { showRemoveProgressDialog = false }
        )
    }
}

interface SaveDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditSave(saveId: Long)

    fun onRemoveSave(saveId : Long)
    fun onNavigateToDeposit()
    fun onNavigateToWithdraw()
    fun onNavigateToTransactionDetail(transactionId: Long)
}