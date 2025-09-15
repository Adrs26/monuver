package com.android.monu.ui.feature.screen.account.accountDetail

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
import com.android.monu.R
import com.android.monu.domain.model.account.Account
import com.android.monu.ui.feature.screen.account.accountDetail.components.AccountConfirmationDialog
import com.android.monu.ui.feature.screen.account.accountDetail.components.AccountDetailAppBar
import com.android.monu.ui.feature.screen.account.accountDetail.components.AccountDetailContent
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.showMessageWithToast

@Composable
fun AccountDetailScreen(
    account: Account,
    editResult: DatabaseResultMessage?,
    accountActions: AccountDetailActions
) {
    var showDeactivateConfirmationDialog by remember { mutableStateOf(false) }
    var showActivateConfirmationDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(editResult) {
        editResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
        }
    }

    Scaffold(
        topBar = {
            AccountDetailAppBar(
                isAccountActive = account.isActive,
                onNavigateBack = accountActions::onNavigateBack,
                onNavigateToEditAccount = { accountActions.onNavigateToEditAccount(account.id) },
                onDeactivateAccount = { showDeactivateConfirmationDialog = true },
                onActivateAccount = { showActivateConfirmationDialog = true }
            )
        }
    ) { innerPadding ->
        AccountDetailContent(
            account = account,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 32.dp)
        )
    }

    if (showDeactivateConfirmationDialog) {
        AccountConfirmationDialog(
            text = stringResource(R.string.deactivate_account_confirmation),
            onDismissRequest = { showDeactivateConfirmationDialog = false },
            onConfirmRequest = {
                accountActions.onDeactivateAccount(account.id)
                showDeactivateConfirmationDialog = false
            }
        )
    }

    if (showActivateConfirmationDialog) {
        AccountConfirmationDialog(
            text = stringResource(R.string.activate_account_confirmation),
            onDismissRequest = { showActivateConfirmationDialog = false },
            onConfirmRequest = {
                accountActions.onActivateAccount(account.id)
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