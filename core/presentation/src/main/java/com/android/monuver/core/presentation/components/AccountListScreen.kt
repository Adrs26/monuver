package com.android.monuver.core.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.presentation.R
import com.android.monuver.core.presentation.util.debouncedClickable

@Composable
fun AccountListScreen(
    accounts: List<AccountState>,
    onNavigateBack: () -> Unit,
    onAccountSelect: (Int, String) -> Unit,
    onNavigateToAddAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.choose_account),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (accounts.isNotEmpty()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(
                    count = accounts.size,
                    key = { index -> accounts[index].id }
                ) { index ->
                    AccountListItem(
                        accountState = accounts[index],
                        modifier = Modifier
                            .debouncedClickable {
                                onAccountSelect(accounts[index].id, accounts[index].name)
                                onNavigateBack()
                            }
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                    )
                }
            }
        } else {
            EmptyAccountContent { onNavigateToAddAccount() }
        }
    }
}