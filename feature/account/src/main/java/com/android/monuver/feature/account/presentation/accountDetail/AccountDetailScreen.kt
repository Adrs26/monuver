package com.android.monuver.feature.account.presentation.accountDetail

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
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.presentation.components.ConfirmationDialog
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.feature.account.R
import com.android.monuver.feature.account.presentation.accountDetail.components.AccountDetailAppBar
import com.android.monuver.feature.account.presentation.accountDetail.components.AccountDetailContent

@Composable
internal fun AccountDetailScreen(
    accountState: AccountState,
    result: DatabaseResultState?,
    accountActions: AccountDetailActions
) {
    val context = LocalContext.current

    var showDeactivateConfirmationDialog by remember { mutableStateOf(false) }
    var showActivateConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        result?.showToast(context)
    }

    Scaffold(
        topBar = {
            AccountDetailAppBar(
                isAccountActive = accountState.isActive,
                onNavigateBack = accountActions::onNavigateBack,
                onNavigateToEditAccount = { accountActions.onNavigateToEditAccount(accountState.id) },
                onDeactivateAccount = { showDeactivateConfirmationDialog = true },
                onActivateAccount = { showActivateConfirmationDialog = true }
            )
        }
    ) { innerPadding ->
        AccountDetailContent(
            accountState = accountState,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 32.dp)
        )
    }

    if (showDeactivateConfirmationDialog) {
        ConfirmationDialog(
            text = stringResource(R.string.deactivate_account_confirmation),
            onDismissRequest = { showDeactivateConfirmationDialog = false },
            onConfirmRequest = {
                accountActions.onDeactivateAccount(accountState.id)
                showDeactivateConfirmationDialog = false
            }
        )
    }

    if (showActivateConfirmationDialog) {
        ConfirmationDialog(
            text = stringResource(R.string.activate_account_confirmation),
            onDismissRequest = { showActivateConfirmationDialog = false },
            onConfirmRequest = {
                accountActions.onActivateAccount(accountState.id)
                showActivateConfirmationDialog = false
            }
        )
    }
}

interface AccountDetailActions {
    fun onNavigateBack()
    fun onNavigateToEditAccount(accountId: Int)
    fun onDeactivateAccount(accountId: Int)
    fun onActivateAccount(accountId: Int)
}