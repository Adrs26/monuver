package com.android.monu.ui.feature.screen.saving.savedetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.android.monu.R
import com.android.monu.domain.model.save.Save
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailAppBar
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailBottomBar
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailContent

@Composable
fun SaveDetailScreen(
    save: Save,
    transactions: List<Transaction>,
    saveActions: SaveDetailActions
) {
    Scaffold(
        topBar = {
            SaveDetailAppBar(
                title = stringResource(R.string.save_detail),
                isActive = save.isActive,
                onNavigateBack = saveActions::onNavigateBack,
                onNavigateToEditSave = { saveActions.onNavigateToEditSave(save.id) }
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
}

interface SaveDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditSave(saveId: Long)
    fun onNavigateToDeposit()
    fun onNavigateToWithdraw()
    fun onNavigateToTransactionDetail(transactionId: Long)
}