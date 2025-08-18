package com.android.monu.presentation.screen.budgeting.budgetdetail

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
import com.android.monu.presentation.screen.budgeting.budgetdetail.components.BudgetDetailAppBar
import com.android.monu.presentation.screen.budgeting.budgetdetail.components.BudgetDetailContent
import com.android.monu.presentation.screen.budgeting.budgetdetail.components.DeleteBudgetDialog
import com.android.monu.presentation.utils.showMessageWithToast

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
                title = stringResource(R.string.budgeting_detail),
                isBudgetActive = budgetState.isActive,
                onNavigateBack = { budgetActions.onNavigateBack() },
                onNavigateToEditBudget = { budgetActions.onNavigateToEditBudget(budgetState.id) },
                onRemoveBudget = { showRemoveDialog = true }
            )
        }
    ) { innerPadding ->
        BudgetDetailContent(
            budgetState = budgetState,
            transactions = transactions,
            onNavigateToTransactionDetail = { budgetActions.onNavigateToTransactionDetail(it) },
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showRemoveDialog) {
        DeleteBudgetDialog(
            onDismissRequest = { showRemoveDialog = false },
            onRemoveBudget = {
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
    val period: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: Long,
    val usedAmount: Long,
    val isActive: Boolean
)

interface BudgetDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditBudget(budgetingId: Long)
    fun onRemoveBudget(budgetingId: Long)
    fun onNavigateToTransactionDetail(transactionId: Long)
}