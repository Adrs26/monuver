package com.android.monuver.feature.budgeting.presentation.budgetDetail

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
import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.model.TransactionListItemState
import com.android.monuver.core.presentation.components.ConfirmationDialog
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.feature.budgeting.R
import com.android.monuver.feature.budgeting.presentation.budgetDetail.components.BudgetDetailAppBar
import com.android.monuver.feature.budgeting.presentation.budgetDetail.components.BudgetDetailContent

@Composable
internal fun BudgetDetailScreen(
    budgetState: BudgetState,
    transactions: List<TransactionListItemState>,
    budgetActions: BudgetDetailActions,
) {
    val context = LocalContext.current

    var showRemoveDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BudgetDetailAppBar(
                isBudgetActive = budgetState.isActive,
                onNavigateBack = budgetActions::onNavigateBack,
                onNavigateToEditBudget = { budgetActions.onNavigateToEditBudget(budgetState.id) },
                onRemoveBudget = { showRemoveDialog = true }
            )
        }
    ) { innerPadding ->
        BudgetDetailContent(
            budgetState = budgetState,
            transactions = transactions,
            onNavigateToTransactionDetail = budgetActions::onNavigateToTransactionDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showRemoveDialog) {
        ConfirmationDialog(
            text = stringResource(R.string.delete_this_budgeting),
            onDismissRequest = { showRemoveDialog = false },
            onConfirmRequest = {
                showRemoveDialog = false
                budgetActions.onRemoveBudget(budgetState.id)
                context.getString(R.string.budgeting_successfully_deleted).showMessageWithToast(context)
                budgetActions.onNavigateBack()
            }
        )
    }
}

internal interface BudgetDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditBudget(budgetId: Long)
    fun onRemoveBudget(budgetId: Long)
    fun onNavigateToTransactionDetail(transactionId: Long)
}