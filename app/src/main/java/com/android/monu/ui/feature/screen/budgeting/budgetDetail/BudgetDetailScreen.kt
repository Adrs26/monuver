package com.android.monu.ui.feature.screen.budgeting.budgetDetail

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
import com.android.monu.R
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.ui.feature.components.ConfirmationDialog
import com.android.monu.ui.feature.screen.budgeting.budgetDetail.components.BudgetDetailAppBar
import com.android.monu.ui.feature.screen.budgeting.budgetDetail.components.BudgetDetailContent
import com.android.monu.ui.feature.utils.showMessageWithToast

@Composable
fun BudgetDetailScreen(
    budgetState: BudgetDetailState,
    transactions: List<Transaction>,
    budgetActions: BudgetDetailActions,
) {
    var showRemoveDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

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

data class BudgetDetailState(
    val id: Long,
    val category: Int,
    val cycle: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: Long,
    val usedAmount: Long,
    val isActive: Boolean
)

interface BudgetDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditBudget(budgetId: Long)
    fun onRemoveBudget(budgetId: Long)
    fun onNavigateToTransactionDetail(transactionId: Long)
}