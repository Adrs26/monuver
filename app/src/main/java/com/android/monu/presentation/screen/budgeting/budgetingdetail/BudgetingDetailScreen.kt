package com.android.monu.presentation.screen.budgeting.budgetingdetail

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
import com.android.monu.presentation.screen.budgeting.budgetingdetail.components.BudgetingDetailAppBar
import com.android.monu.presentation.screen.budgeting.budgetingdetail.components.BudgetingDetailContent
import com.android.monu.presentation.screen.budgeting.budgetingdetail.components.DeleteBudgetingDialog
import com.android.monu.presentation.utils.showMessageWithToast

@Composable
fun BudgetingDetailScreen(
    budgetingState: BudgetingDetailState,
    transactions: List<Transaction>,
    onNavigateBack: () -> Unit,
    onNavigateToTransactionDetail: (Long) -> Unit,
    onRemoveBudgeting: (Long) -> Unit,
) {
    var showRemoveDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BudgetingDetailAppBar(
                title = stringResource(R.string.budgeting_detail),
                onNavigateBack = onNavigateBack,
                onEditClick = {},
                onDeleteClick = { showRemoveDialog = true }
            )
        }
    ) { innerPadding ->
        BudgetingDetailContent(
            budgetingState = budgetingState,
            transactions = transactions,
            onNavigateToTransactionDetail = onNavigateToTransactionDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (showRemoveDialog) {
        DeleteBudgetingDialog(
            onDismissRequest = { showRemoveDialog = false },
            onRemoveBudgeting = {
                showRemoveDialog = false
                onRemoveBudgeting(budgetingState.id)
                context.getString(R.string.budgeting_successfully_deleted).showMessageWithToast(context)
                onNavigateBack()
            }
        )
    }
}

data class BudgetingDetailState(
    val id: Long,
    val category: Int,
    val period: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: Long,
    val usedAmount: Long
)