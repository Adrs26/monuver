package com.android.monu.presentation.screen.transaction.addtransaction.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.account.Account
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.screen.account.components.AccountListItem
import com.android.monu.ui.theme.MonuTheme

@Composable
fun AddTransactionSourceScreen(
    accounts: List<Account>,
    onNavigateBack: () -> Unit,
    onSourceSelect: (Int, String) -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = "Pilih sumber dana",
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
                    AccountListItem(
                        account = accounts[index],
                        modifier = Modifier
                            .clickable {
                                onSourceSelect(accounts[index].id, accounts[index].name)
                                onNavigateBack()
                            }
                            .padding(horizontal = 16.dp, vertical = 2.dp)
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
fun AddTransactionSourceScreenPreview() {
    MonuTheme {
        AddTransactionSourceScreen(
            accounts = emptyList(),
            onNavigateBack = {},
            onSourceSelect = { _, _ -> }
        )
    }
}