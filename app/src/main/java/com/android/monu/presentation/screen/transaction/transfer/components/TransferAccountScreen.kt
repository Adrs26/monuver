package com.android.monu.presentation.screen.transaction.transfer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.domain.model.account.Account
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.screen.account.components.AccountListItem
import com.android.monu.ui.theme.MonuTheme
import com.android.monu.presentation.utils.SelectAccountType

@Composable
fun TransferAccountScreen(
    selectAccountType: Int,
    accounts: List<Account>,
    selectedAccounts: List<Int>,
    onNavigateBack: () -> Unit,
    onAccountSelect: (Int, String) -> Unit
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
                modifier = Modifier
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
                        account = accounts[index],
                        modifier = if (!isAccountSelected) {
                            Modifier
                                .clickable {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Kamu belum memiliki akun",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TransferAccountScreenPreview() {
    MonuTheme {
        TransferAccountScreen(
            selectAccountType = SelectAccountType.SOURCE,
            accounts = emptyList(),
            selectedAccounts = emptyList(),
            onNavigateBack = {},
            onAccountSelect = { _, _ -> }
        )
    }
}