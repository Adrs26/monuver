package com.android.monuver.feature.transaction.presentation.transfer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.presentation.components.AccountListItem
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.EmptyAccountContent
import com.android.monuver.core.presentation.util.debouncedClickable
import com.android.monuver.core.domain.util.SelectAccountType
import com.android.monuver.feature.transaction.R

@Composable
internal fun TransferAccountListScreen(
    selectAccountType: Int,
    accounts: List<AccountState>,
    selectedAccounts: List<Int>,
    onNavigateBack: () -> Unit,
    onAccountSelect: (Int, String) -> Unit,
    onNavigateToAddAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = if (selectAccountType == SelectAccountType.SOURCE)
                    stringResource(R.string.choose_source_account) else
                        stringResource(R.string.choose_destination_account),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (accounts.isNotEmpty()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                items(
                    count = accounts.size,
                    key = { index -> accounts[index].id }
                ) { index ->
                    val isAccountSelected = accounts[index].id in selectedAccounts

                    AccountListItem(
                        accountState = accounts[index],
                        modifier = if (!isAccountSelected) {
                            Modifier
                                .debouncedClickable {
                                    onAccountSelect(accounts[index].id, accounts[index].name)
                                    onNavigateBack()
                                }
                                .padding(horizontal = 16.dp, vertical = 2.dp)
                        } else {
                            Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 16.dp, vertical = 2.dp)
                        },
                        containerColor = if (!isAccountSelected) {
                            MaterialTheme.colorScheme.background
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                }
            }
        } else {
            EmptyAccountContent { onNavigateToAddAccount() }
        }
    }
}