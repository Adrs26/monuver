package com.android.monuver.feature.account.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.CommonFloatingActionButton
import com.android.monuver.feature.account.R
import com.android.monuver.feature.account.presentation.components.AccountContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountScreen(
    accounts: List<AccountState>,
    totalBalance: Long,
    accountActions: AccountActions
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.account),
                onNavigateBack = accountActions::onNavigateBack
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton { accountActions.onNavigateToAddAccount() }
        }
    ) { innerPadding ->
        AccountContent(
            accounts = accounts,
            totalBalance = totalBalance,
            onNavigateToAccountDetail = accountActions::onNavigateToAccountDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

internal interface AccountActions {
    fun onNavigateBack()
    fun onNavigateToAccountDetail(accountId: Int)
    fun onNavigateToAddAccount()
}